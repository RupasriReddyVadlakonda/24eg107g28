import { useCallback, useMemo, useState } from 'react';
import { FileCheck2 } from 'lucide-react';
import { ConsentDecisionDialog } from '../../components/consents/ConsentDecisionDialog';
import { ConsentStatusBadge } from '../../components/consents/ConsentStatusBadge';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { PageHeader } from '../../components/ui/PageHeader';
import { useToast } from '../../hooks/useToast';
import { useResource } from '../../hooks/useResource';
import { consentService } from '../../services/consentService';
import type { Consent } from '../../types/api';
import { formatDate, labelForDataType } from '../../utils/format';

export function ConsentRequestsPage() {
  const load = useCallback(() => consentService.list(), []);
  const { data: consents, loading, error, reload } = useResource(load);
  const { showToast } = useToast();
  const [busyId, setBusyId] = useState<number | null>(null);
  const pending = useMemo(() => (consents ?? []).filter((item) => item.status === 'PENDING'), [consents]);
  const decide = useCallback(async (item: Consent, action: 'grant' | 'deny') => {
    setBusyId(item.id);
    try {
      if (action === 'grant') await consentService.grant(item.id);
      else await consentService.deny(item.id);
      showToast(action === 'grant' ? 'Consent granted.' : 'Consent request denied.', 'success');
      await reload();
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Could not update this request.', 'error');
    } finally { setBusyId(null); }
  }, [reload, showToast]);
  const columns = useMemo<Column<Consent>[]>(() => [
    { key: 'application', header: 'Application', render: (row) => <span className="font-semibold">{row.applicationName}</span> },
    { key: 'requested', header: 'Requested access', render: (row) => <div><p>{labelForDataType(row.dataType)} · {row.allowedOperation}</p><p className="mt-1 text-xs text-vault-secondary">{row.purpose}</p></div> },
    { key: 'duration', header: 'Duration', render: (row) => `${row.requestedDurationDays} day${row.requestedDurationDays === 1 ? '' : 's'}` },
    { key: 'requestedAt', header: 'Requested', render: (row) => <span className="text-xs text-vault-secondary">{formatDate(row.createdAt)}</span> },
    { key: 'decision', header: 'Decision', render: (row) => <ConsentDecisionDialog consent={row} onAction={(action) => decide(row, action)} busy={busyId === row.id} /> },
  ], [busyId, decide]);

  return <>
    <PageHeader title="Consent Requests" description="Review what each application wants to access, why, and for how long." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading consent requests" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !pending.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No consent requests to review" detail="New requests from applications will appear here." /></div>
      : <>
        <p className="mb-4 flex items-center gap-2 text-sm text-vault-secondary"><FileCheck2 size={16} />{pending.length} pending request{pending.length === 1 ? '' : 's'}</p>
        <div className="hidden md:block"><DataTable caption="Pending requests for access to personal data" columns={columns} rows={pending} /></div>
        <div className="space-y-3 md:hidden">
          {pending.map((item) => <article key={item.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4">
            <div className="flex items-start justify-between gap-3"><h2 className="font-semibold text-vault-lit">{item.applicationName}</h2><ConsentStatusBadge status={item.status} /></div>
            <dl className="mt-4 grid grid-cols-[auto_1fr] gap-x-4 gap-y-2 text-sm">
              <dt className="text-vault-secondary">Data</dt><dd>{labelForDataType(item.dataType)}</dd>
              <dt className="text-vault-secondary">Operation</dt><dd>{item.allowedOperation}</dd>
              <dt className="text-vault-secondary">Purpose</dt><dd>{item.purpose}</dd>
              <dt className="text-vault-secondary">Duration</dt><dd>{item.requestedDurationDays} days</dd>
            </dl>
            <div className="mt-5"><ConsentDecisionDialog consent={item} onAction={(action) => decide(item, action)} busy={busyId === item.id} /></div>
          </article>)}
        </div>
      </>}
  </>;
}
