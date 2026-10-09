import { useCallback, useMemo, useState } from 'react';
import { AlertTriangle, CheckCircle2 } from 'lucide-react';
import { PageHeader } from '../../components/ui/PageHeader';
import { Button } from '../../components/ui/Button';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { ConfirmDialog } from '../../components/ui/Dialog';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { useResource } from '../../hooks/useResource';
import { useToast } from '../../hooks/useToast';
import { securityService } from '../../services/securityService';
import type { SecurityAlert } from '../../types/api';
import { formatDateTime } from '../../utils/format';

export function SecurityAlertsPage() {
  const load = useCallback(() => securityService.alerts(), []);
  const { data: alerts, loading, error, reload } = useResource(load);
  const { showToast } = useToast();
  const [resolveTarget, setResolveTarget] = useState<SecurityAlert | null>(null);
  const [busy, setBusy] = useState(false);
  const ordered = useMemo(() => [...(alerts ?? [])].sort((a, b) => Number(a.resolved) - Number(b.resolved) || new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()), [alerts]);
  const resolve = async () => {
    if (!resolveTarget) return;
    setBusy(true);
    try {
      await securityService.resolveOwnAlert(resolveTarget.id);
      setResolveTarget(null);
      showToast('Security alert resolved.', 'success');
      await reload();
    } catch (reason) { showToast(reason instanceof Error ? reason.message : 'Could not resolve this alert.', 'error'); }
    finally { setBusy(false); }
  };

  return <>
    <PageHeader title="Security Alerts" description="Review alerts generated from unusual or repeated access attempts." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading security alerts" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !ordered.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No security alerts" detail="Alerts will appear here if the service detects repeated unauthorized access or other suspicious activity." /></div>
      : <div className="divide-y divide-vault-keyline border-y border-vault-keyline">
        {ordered.map((alert) => <article key={alert.id} className="grid gap-4 py-5 sm:grid-cols-[minmax(0,1fr)_auto] sm:items-start">
          <div className="flex gap-3">
            {alert.resolved ? <CheckCircle2 size={19} className="mt-0.5 shrink-0 text-vault-secondary" aria-hidden="true" /> : <AlertTriangle size={19} className="mt-0.5 shrink-0 text-[#F0A497]" aria-hidden="true" />}
            <div className="min-w-0"><div className="flex flex-wrap items-center gap-2"><h2 className="font-semibold">{alert.alertType.replaceAll('_', ' ').toLowerCase().replace(/\b\w/g, (letter) => letter.toUpperCase())}</h2><StatusBadge>{alert.severity}</StatusBadge><StatusBadge>{alert.resolved ? 'RESOLVED' : 'OPEN'}</StatusBadge></div>
              <p className="mt-2 max-w-3xl text-sm leading-6 text-vault-secondary">{alert.description}</p>
              <p className="mt-2 text-xs text-vault-olive">{formatDateTime(alert.timestamp)} · Application ID {alert.applicationId ?? 'unavailable'}</p></div>
          </div>
          {!alert.resolved && <Button variant="outline" className="sm:mt-0" onClick={() => setResolveTarget(alert)}>Resolve alert</Button>}
        </article>)}
      </div>}
    <ConfirmDialog open={!!resolveTarget} title="Resolve this alert?" description={`Mark ${resolveTarget?.alertType.replaceAll('_', ' ').toLowerCase() ?? 'this alert'} as resolved. The alert record will remain in your history.`} confirmLabel="Resolve alert" busy={busy} onConfirm={() => void resolve()} onCancel={() => setResolveTarget(null)} />
  </>;
}
