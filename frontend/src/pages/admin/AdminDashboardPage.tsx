import { useCallback, useMemo } from 'react';
import { Activity, Bell, Blocks, FileCheck2, Users, type LucideIcon } from 'lucide-react';
import { Link } from 'react-router-dom';
import { PageHeader } from '../../components/ui/PageHeader';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { auditService } from '../../services/auditService';
import { securityService } from '../../services/securityService';
import type { AccessLog } from '../../types/api';
import type { AdminData } from '../../types/admin';
import { formatDateTime, labelForDataType } from '../../utils/format';
import { useResource } from '../../hooks/useResource';

const initial: AdminData = { users: null, apps: null, consents: null, alerts: null, audit: null, errors: [] };
function Metric({ label, value, note, Icon, to }: { label: string; value: string; note: string; Icon: LucideIcon; to: string }) {
  return <Link to={to} className="block border-b border-vault-keyline py-4 hover:bg-white/[0.025] sm:py-5"><div className="flex items-center gap-3"><Icon size={18} className="text-vault-secondary" /><span className="text-sm text-vault-secondary">{label}</span><span className="ml-auto font-display text-2xl text-vault-lit">{value}</span></div><p className="mt-1 pl-8 text-xs text-vault-olive">{note}</p></Link>;
}

export function AdminDashboardPage() {
  const load = useCallback(async (): Promise<AdminData> => {
    const results = await Promise.allSettled([securityService.adminUsers(), securityService.adminApps(), securityService.adminConsents(), securityService.adminAlerts(), auditService.adminPage(0, 100)]);
    const next: AdminData = { ...initial, errors: [] };
    if (results[0].status === 'fulfilled') next.users = results[0].value; else next.errors.push('users');
    if (results[1].status === 'fulfilled') next.apps = results[1].value; else next.errors.push('applications');
    if (results[2].status === 'fulfilled') next.consents = results[2].value; else next.errors.push('consents');
    if (results[3].status === 'fulfilled') next.alerts = results[3].value; else next.errors.push('alerts');
    if (results[4].status === 'fulfilled') next.audit = results[4].value.records; else next.errors.push('audit');
    return next;
  }, []);
  const { data: response, loading, error, reload } = useResource(load);
  const data = response ?? initial;
  const unresolved = useMemo(() => data.alerts?.filter((alert) => !alert.resolved) ?? [], [data.alerts]);

  return <>
    <PageHeader title="Admin Dashboard" description="System-wide account, application, consent, and access records." />
    <div className="mb-6 rounded-lg border border-vault-pending/70 bg-vault-pending/10 px-4 py-3 text-sm text-vault-lit">Administrator view. Totals are derived from records returned by the administration APIs.</div>
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading system records" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={reload} /></div>
      : <>
        {data.errors.length > 0 && <div className="mb-5 rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={new Error(`Some system records are unavailable: ${data.errors.join(', ')}.`)} onRetry={reload} /></div>}
        <section className="grid gap-8 xl:grid-cols-[minmax(0,1fr)_minmax(18rem,.7fr)]">
          <div><h2 className="mb-3 font-display text-xl">System summary</h2><div className="border-y border-vault-keyline">
            <Metric label="Users" value={data.users ? String(data.users.length) : '—'} note="Returned by the admin users endpoint" Icon={Users} to="/admin/users" />
            <Metric label="Applications" value={data.apps ? String(data.apps.length) : '—'} note="Registered applications" Icon={Blocks} to="/admin/applications" />
            <Metric label="Consent records" value={data.consents ? String(data.consents.length) : '—'} note="Consent decisions returned" Icon={FileCheck2} to="/admin" />
            <Metric label="Unresolved alerts" value={data.alerts ? String(unresolved.length) : '—'} note="All alert records returned" Icon={Bell} to="/admin/security-alerts" />
            <Metric label="Access records" value={data.audit ? String(data.audit.length) : '—'} note="Current audit page (latest 100)" Icon={Activity} to="/admin/audit" />
          </div></div>
          <section aria-labelledby="system-alerts-title"><div className="mb-3 flex items-center justify-between"><h2 id="system-alerts-title" className="font-display text-xl">Unresolved alerts</h2><Link to="/admin/security-alerts" className="text-sm text-vault-secondary underline underline-offset-4">All alerts</Link></div>
            {unresolved.length ? <div className="divide-y divide-vault-keyline border-y border-vault-keyline">{unresolved.slice(0, 5).map((alert) => <div key={alert.id} className="py-3"><div className="flex flex-wrap items-center gap-2"><strong className="text-sm">{alert.alertType.replaceAll('_', ' ')}</strong><StatusBadge>{alert.severity}</StatusBadge></div><p className="mt-1 text-xs text-vault-secondary">{formatDateTime(alert.timestamp)} · {alert.description}</p></div>)}</div> : <div className="border-y border-vault-keyline"><EmptyState title="No unresolved alerts" detail="No open alert records were returned." /></div>}
          </section>
        </section>
        <section className="mt-9"><div className="mb-3 flex items-baseline justify-between"><h2 className="font-display text-xl">Recent system access</h2><Link to="/admin/audit" className="text-sm text-vault-secondary underline underline-offset-4">All audit logs</Link></div>
          {!data.audit?.length ? <div className="border-y border-vault-keyline"><EmptyState title="No system audit records" detail="No access events were returned by the API." /></div>
            : <RecentAdminRecords records={data.audit} />}
        </section>
      </>}
  </>;
}

function RecentAdminRecords({ records }: { records: AccessLog[] }) {
  return <div className="overflow-x-auto rounded-lg border border-vault-keyline"><table className="w-full min-w-[40rem] text-left text-sm"><thead className="bg-vault-nav text-xs uppercase text-vault-secondary"><tr><th className="px-4 py-3">Timestamp</th><th className="px-4 py-3">User ID</th><th className="px-4 py-3">Application</th><th className="px-4 py-3">Data / operation</th><th className="px-4 py-3">Result</th></tr></thead><tbody className="divide-y divide-vault-keyline/70">{records.slice(0, 6).map((record) => <tr key={record.id}><td className="whitespace-nowrap px-4 py-3 text-xs text-vault-secondary">{formatDateTime(record.timestamp)}</td><td className="px-4 py-3 font-mono text-xs">{record.userId}</td><td className="px-4 py-3">{record.applicationName || 'Unknown application'}</td><td className="px-4 py-3">{labelForDataType(record.dataType)} · {record.operation}</td><td className="px-4 py-3"><StatusBadge>{record.success ? 'SUCCESS' : 'FAILURE'}</StatusBadge></td></tr>)}</tbody></table></div>;
}
