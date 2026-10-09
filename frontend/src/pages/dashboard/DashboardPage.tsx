import { useCallback } from 'react';
import { Activity, AlertTriangle, Database, FileCheck2, KeyRound, ShieldCheck, type LucideIcon } from 'lucide-react';
import { Link } from 'react-router-dom';
import { PageHeader } from '../../components/ui/PageHeader';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { AccessCharts } from '../../components/dashboard/AccessCharts';
import { auditService } from '../../services/auditService';
import { vaultService } from '../../services/vaultService';
import { consentService } from '../../services/consentService';
import { securityService } from '../../services/securityService';
import type { AccessLog, Consent, PersonalData, SecurityAlert } from '../../types/api';
import { formatDateTime, labelForDataType } from '../../utils/format';
import { useResource } from '../../hooks/useResource';

interface DashboardState {
  vault: PersonalData[] | null;
  consents: Consent[] | null;
  audit: AccessLog[] | null;
  alerts: SecurityAlert[] | null;
  errors: string[];
}
const emptyState: DashboardState = { vault: null, consents: null, audit: null, alerts: null, errors: [] };

function Metric({ label, value, note, icon: Icon, to }: { label: string; value: string; note: string; icon: LucideIcon; to: string }) {
  return <Link to={to} className="group block border-b border-vault-keyline py-4 first:border-t sm:py-5">
    <div className="flex items-center gap-3"><Icon size={18} className="shrink-0 text-vault-secondary" aria-hidden="true" /><span className="text-sm text-vault-secondary">{label}</span><span className="ml-auto font-display text-2xl text-vault-lit group-hover:text-vault-secondary">{value}</span></div>
    <p className="mt-1 pl-[1.95rem] text-xs text-vault-olive">{note}</p>
  </Link>;
}

export function DashboardPage() {
  const load = useCallback(async (): Promise<DashboardState> => {
    const results = await Promise.allSettled([vaultService.list(), consentService.list(), auditService.ownPage(0, 100), securityService.alerts()]);
    const next: DashboardState = { ...emptyState, errors: [] };
    if (results[0].status === 'fulfilled') next.vault = results[0].value; else next.errors.push('vault');
    if (results[1].status === 'fulfilled') next.consents = results[1].value; else next.errors.push('consents');
    if (results[2].status === 'fulfilled') next.audit = results[2].value.records; else next.errors.push('audit');
    if (results[3].status === 'fulfilled') next.alerts = results[3].value; else next.errors.push('alerts');
    return next;
  }, []);
  const { data: response, loading, error, reload } = useResource(load);
  const data = response ?? emptyState;
  const pending = (data.consents ?? []).filter((item) => item.status === 'PENDING');
  const current = (data.consents ?? []).filter((item) => item.status === 'GRANTED' && (!item.expirationTime || new Date(item.expirationTime) > new Date()));
  const openAlerts = (data.alerts ?? []).filter((item) => !item.resolved);
  const recent = [...(data.audit ?? [])].sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()).slice(0, 5);

  return <>
    <PageHeader title="Dashboard" description="A current view of your stored data, permissions, and access history." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Gathering account records" /></div>
      : error ? <div className="mb-6 rounded-xl border border-vault-keyline bg-vault-well p-4"><ErrorState error={error} onRetry={reload} /></div>
      : data.errors.length > 0 && <div className="mb-6 rounded-xl border border-vault-keyline bg-vault-well p-4"><ErrorState error={new Error(`Some dashboard records are unavailable: ${data.errors.join(', ')}.`)} onRetry={reload} /></div>}
    {!loading && !error && <>
      <div className="grid gap-8 xl:grid-cols-[minmax(0,1.2fr)_minmax(20rem,.8fr)]">
        <section aria-labelledby="decisions-heading">
          <div className="mb-3 flex items-center justify-between gap-3"><h2 id="decisions-heading" className="font-display text-2xl">Needs your review</h2><Link to="/consents/requests" className="text-sm text-vault-secondary underline underline-offset-4">All requests</Link></div>
          {pending.length ? <div className="divide-y divide-vault-keyline border-y border-vault-keyline">{pending.slice(0, 4).map((item) => <Link key={item.id} to="/consents/requests" className="grid gap-2 py-4 transition-colors hover:bg-white/[0.025] sm:grid-cols-[1fr_auto] sm:items-center sm:px-2"><div><div className="flex flex-wrap items-center gap-2"><h3 className="font-semibold">{item.applicationName}</h3><StatusBadge>{item.status}</StatusBadge></div><p className="mt-1 text-sm text-vault-secondary">{labelForDataType(item.dataType)} · {item.allowedOperation} · {item.purpose}</p></div><span className="text-xs text-vault-secondary">{item.requestedDurationDays} days <span aria-hidden="true">→</span></span></Link>)}</div>
            : <div className="border-y border-vault-keyline"><EmptyState title="No consent requests to review" detail="New application requests will be listed here." /></div>}
          {openAlerts.length > 0 && <Link to="/security-alerts" className="mt-5 flex items-start gap-3 rounded-lg border border-vault-alert/70 bg-vault-alert/10 p-4"><AlertTriangle size={18} className="mt-0.5 shrink-0 text-[#F0A497]" /><span><strong className="block text-sm">{openAlerts.length} unresolved security alert{openAlerts.length === 1 ? '' : 's'}</strong><span className="mt-1 block text-xs text-vault-secondary">Review recent access activity.</span></span></Link>}
        </section>
        <section aria-labelledby="summary-heading"><div className="mb-3 flex items-baseline justify-between"><h2 id="summary-heading" className="font-display text-2xl">Account summary</h2></div><div className="border-y border-vault-keyline">
          <Metric label="Total data items" value={data.vault ? String(data.vault.length) : '—'} note={data.vault ? 'Stored in your encrypted vault' : 'Vault records unavailable'} icon={Database} to="/vault" />
          <Metric label="Active consents" value={data.consents ? String(current.length) : '—'} note={data.consents ? 'Granted and not expired' : 'Consent records unavailable'} icon={KeyRound} to="/consents" />
          <Metric label="Pending requests" value={data.consents ? String(pending.length) : '—'} note={data.consents ? 'Awaiting your decision' : 'Consent records unavailable'} icon={FileCheck2} to="/consents/requests" />
          <Metric label="Recent accesses" value={data.audit ? String(data.audit.length) : '—'} note={data.audit ? 'Loaded access records (latest page)' : 'Audit records unavailable'} icon={Activity} to="/audit" />
          <Metric label="Open alerts" value={data.alerts ? String(openAlerts.length) : '—'} note={data.alerts ? 'Unresolved security alerts' : 'Alert records unavailable'} icon={ShieldCheck} to="/security-alerts" />
        </div></section>
      </div>
      <section className="mt-9" aria-labelledby="evidence-heading"><div className="mb-4 flex flex-wrap items-end justify-between gap-2"><div><h2 id="evidence-heading" className="font-display text-2xl">Access evidence</h2><p className="mt-1 text-xs text-vault-secondary">Charts use recorded audit events only.</p></div><Link to="/audit" className="text-sm text-vault-secondary underline underline-offset-4">View access history</Link></div>
        {data.audit === null ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="Access charts unavailable" detail="Audit records could not be loaded. Retry to see verified access history." /></div> : <AccessCharts records={data.audit} />}
      </section>
      <section className="mt-9" aria-labelledby="recent-heading"><div className="mb-3 flex items-end justify-between gap-4"><div><h2 id="recent-heading" className="font-display text-2xl">Recent access</h2><p className="mt-1 text-xs text-vault-secondary">From the latest audit records available.</p></div><Link to="/audit" className="text-sm text-vault-secondary underline underline-offset-4">Full history</Link></div>
        {!recent.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No access records found" detail="Access history will appear when an application attempts to use personal data." /></div>
          : <div className="overflow-x-auto rounded-lg border border-vault-keyline"><table className="w-full min-w-[35rem] text-left text-sm"><thead className="bg-vault-nav text-xs uppercase text-vault-secondary"><tr><th className="px-4 py-3">Time</th><th className="px-4 py-3">Application</th><th className="px-4 py-3">Data / operation</th><th className="px-4 py-3">Result</th></tr></thead><tbody className="divide-y divide-vault-keyline/70">{recent.map((row) => <tr key={row.id}><td className="whitespace-nowrap px-4 py-3 text-xs text-vault-secondary">{formatDateTime(row.timestamp)}</td><td className="px-4 py-3">{row.applicationName || 'Unknown application'}</td><td className="px-4 py-3">{labelForDataType(row.dataType)} · {row.operation || '—'}</td><td className="px-4 py-3"><StatusBadge>{row.success ? 'SUCCESS' : 'FAILURE'}</StatusBadge></td></tr>)}</tbody></table></div>}
      </section>
    </>}
  </>;
}
