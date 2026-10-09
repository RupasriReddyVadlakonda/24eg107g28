import { useCallback, useMemo, useState } from 'react';
import { useResource } from '../../hooks/useResource';
import { consentService } from '../../services/consentService';
import type { Consent, ConsentStatus } from '../../types/api';
import { formatDate, formatDateTime, labelForDataType } from '../../utils/format';
import { PageHeader } from '../../components/ui/PageHeader';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { ConsentStatusBadge } from '../../components/consents/ConsentStatusBadge';
import { Button } from '../../components/ui/Button';
import { ConfirmDialog } from '../../components/ui/Dialog';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { useToast } from '../../hooks/useToast';

function statusOf(consent: Consent): ConsentStatus {
  if (consent.status === 'GRANTED' && consent.expirationTime && new Date(consent.expirationTime) <= new Date()) return 'EXPIRED';
  return consent.status;
}

export function ConsentsPage() {
  const load = useCallback(() => consentService.list(), []);
  const { data: consents, loading, error, reload } = useResource(load);
  const { showToast } = useToast();
  const [revokeTarget, setRevokeTarget] = useState<Consent | null>(null);
  const [busy, setBusy] = useState(false);
  const active = useMemo(() => (consents ?? []).filter((item) => statusOf(item) === 'GRANTED'), [consents]);
  const expired = useMemo(() => (consents ?? []).filter((item) => statusOf(item) === 'EXPIRED'), [consents]);
  const other = useMemo(() => (consents ?? []).filter((item) => !['GRANTED', 'EXPIRED'].includes(statusOf(item))), [consents]);

  const revoke = async () => {
    if (!revokeTarget) return;
    setBusy(true);
    try {
      await consentService.revoke(revokeTarget.id);
      setRevokeTarget(null);
      showToast('Consent revoked. The application can no longer use this permission.', 'success');
      await reload();
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Could not revoke consent.', 'error');
    } finally { setBusy(false); }
  };

  const makeColumns = (allowRevoke: boolean): Column<Consent>[] => [
    { key: 'application', header: 'Application', render: (row) => <span className="font-semibold">{row.applicationName}</span> },
    { key: 'data', header: 'Data type', render: (row) => labelForDataType(row.dataType) },
    { key: 'purpose', header: 'Purpose', render: (row) => <span className="text-vault-secondary">{row.purpose}</span> },
    { key: 'operation', header: 'Operation', render: (row) => row.allowedOperation },
    { key: 'granted', header: 'Granted', render: (row) => <span className="text-xs text-vault-secondary">{formatDateTime(row.startTime)}</span> },
    { key: 'expiry', header: 'Expires', render: (row) => <span className="text-xs text-vault-secondary">{formatDateTime(row.expirationTime)}</span> },
    { key: 'status', header: 'Status', render: (row) => <ConsentStatusBadge status={statusOf(row)} /> },
    ...(allowRevoke ? [{ key: 'actions', header: 'Action', render: (row: Consent) => <Button variant="quiet" className="min-h-8 px-2 text-xs" onClick={() => setRevokeTarget(row)}>Revoke</Button> } as Column<Consent>] : []),
  ];
  const columns = makeColumns(true);
  const historicalColumns = makeColumns(false);

  return <>
    <PageHeader title="Active Consents" description="Review the permissions applications currently hold and revoke access at any time." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading consents" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !consents?.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No consent records" detail="When an application requests access, the request will appear here after you decide." /></div>
      : <div className="space-y-8">
        <section aria-labelledby="current-consents-heading">
          <div className="mb-3 flex items-baseline justify-between"><h2 id="current-consents-heading" className="font-display text-xl">Current permissions</h2><span className="text-sm text-vault-secondary">{active.length} granted</span></div>
          {active.length ? <><div className="hidden md:block"><DataTable caption="Currently granted consents" rows={active} columns={columns} /></div><div className="space-y-3 md:hidden">{active.map((item) => <article key={item.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4"><div className="flex items-start justify-between gap-3"><h3 className="font-semibold">{item.applicationName}</h3><ConsentStatusBadge status="GRANTED" /></div><p className="mt-3 text-sm">{labelForDataType(item.dataType)} · {item.allowedOperation}</p><p className="mt-1 text-sm text-vault-secondary">{item.purpose}</p><p className="mt-3 text-xs text-vault-secondary">Expires {formatDate(item.expirationTime)}</p><Button variant="quiet" className="mt-2 px-0" onClick={() => setRevokeTarget(item)}>Revoke consent</Button></article>)}</div></>
            : <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No active consents" detail="There are no current application permissions." /></div>}
        </section>
        <section aria-labelledby="expired-consents-heading">
          <div className="mb-3 flex items-baseline justify-between"><h2 id="expired-consents-heading" className="font-display text-xl">Expired</h2><span className="text-sm text-vault-secondary">{expired.length} expired</span></div>
          {expired.length ? <div className="hidden md:block"><DataTable caption="Expired consent permissions" rows={expired} columns={historicalColumns} /></div> : <p className="text-sm text-vault-secondary">No expired permissions.</p>}
        </section>
        <section aria-labelledby="past-consents-heading">
          <div className="mb-3 flex items-baseline justify-between"><h2 id="past-consents-heading" className="font-display text-xl">Other requests</h2><span className="text-sm text-vault-secondary">{other.length} records</span></div>
          {other.length ? <div className="hidden md:block"><DataTable caption="Denied, revoked, and pending consents" rows={other} columns={historicalColumns} /></div> : <p className="text-sm text-vault-secondary">No denied or revoked consent requests.</p>}
        </section>
      </div>}
    <ConfirmDialog open={!!revokeTarget} title="Revoke this consent?" description={`This will remove ${revokeTarget?.applicationName ?? 'the application'}’s ${revokeTarget ? labelForDataType(revokeTarget.dataType) : ''} permission for “${revokeTarget?.purpose ?? ''}”. Future requests will require your approval again.`} confirmLabel="Revoke consent" danger busy={busy} onConfirm={() => void revoke()} onCancel={() => setRevokeTarget(null)} />
  </>;
}
