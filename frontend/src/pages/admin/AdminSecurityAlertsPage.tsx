import { useCallback, useMemo, useState } from 'react';
import { PageHeader } from '../../components/ui/PageHeader';
import { Button } from '../../components/ui/Button';
import { ConfirmDialog } from '../../components/ui/Dialog';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { useResource } from '../../hooks/useResource';
import { useToast } from '../../hooks/useToast';
import { securityService } from '../../services/securityService';
import type { SecurityAlert } from '../../types/api';
import { formatDateTime } from '../../utils/format';

export function AdminSecurityAlertsPage() {
  const load = useCallback(() => securityService.adminAlerts(), []);
  const { data: alerts, loading, error, reload } = useResource(load);
  const { showToast } = useToast();
  const [target, setTarget] = useState<SecurityAlert | null>(null);
  const [busy, setBusy] = useState(false);
  const resolve = async () => {
    if (!target) return;
    setBusy(true);
    try { await securityService.resolveAdminAlert(target.id); setTarget(null); showToast('Alert resolved.', 'success'); await reload(); }
    catch (reason) { showToast(reason instanceof Error ? reason.message : 'Could not resolve this alert.', 'error'); }
    finally { setBusy(false); }
  };
  const rows = useMemo(() => [...(alerts ?? [])].sort((a, b) => Number(a.resolved) - Number(b.resolved) || new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()), [alerts]);
  const columns = useMemo<Column<SecurityAlert>[]>(() => [
    { key: 'type', header: 'Alert type', render: (alert) => <span className="font-semibold">{alert.alertType.replaceAll('_', ' ')}</span> },
    { key: 'severity', header: 'Severity', render: (alert) => <StatusBadge>{alert.severity}</StatusBadge> },
    { key: 'application', header: 'Application ID', render: (alert) => alert.applicationId ?? 'Unavailable' },
    { key: 'description', header: 'Description', render: (alert) => <span className="text-vault-secondary">{alert.description}</span> },
    { key: 'timestamp', header: 'Timestamp', render: (alert) => <span className="whitespace-nowrap text-xs text-vault-secondary">{formatDateTime(alert.timestamp)}</span> },
    { key: 'status', header: 'Status', render: (alert) => <StatusBadge>{alert.resolved ? 'RESOLVED' : 'OPEN'}</StatusBadge> },
    { key: 'action', header: 'Action', render: (alert) => !alert.resolved ? <Button variant="quiet" className="min-h-8 px-2 text-xs" onClick={() => setTarget(alert)}>Resolve</Button> : '—' },
  ], []);
  return <>
    <PageHeader title="Security Alerts" description="System-wide alerts and resolution actions." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading alerts" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !rows.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No security alerts" detail="No alert records were returned by the system endpoint." /></div>
      : <><div className="hidden lg:block"><DataTable caption="System-wide security alerts" rows={rows} columns={columns} /></div><div className="space-y-3 lg:hidden">{rows.map((alert) => <article key={alert.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4"><div className="flex flex-wrap items-center gap-2"><h2 className="font-semibold">{alert.alertType.replaceAll('_', ' ')}</h2><StatusBadge>{alert.severity}</StatusBadge><StatusBadge>{alert.resolved ? 'RESOLVED' : 'OPEN'}</StatusBadge></div><p className="mt-2 text-sm text-vault-secondary">{alert.description}</p><p className="mt-2 text-xs text-vault-olive">{formatDateTime(alert.timestamp)} · Application ID {alert.applicationId ?? 'unavailable'}</p>{!alert.resolved && <Button variant="quiet" className="mt-2 px-0" onClick={() => setTarget(alert)}>Resolve alert</Button>}</article>)}</div></>}
    <ConfirmDialog open={!!target} title="Resolve this alert?" description={`Mark ${target?.alertType.replaceAll('_', ' ').toLowerCase() ?? 'this alert'} as resolved. The record will remain visible in system history.`} confirmLabel="Resolve alert" busy={busy} onConfirm={() => void resolve()} onCancel={() => setTarget(null)} />
  </>;
}
