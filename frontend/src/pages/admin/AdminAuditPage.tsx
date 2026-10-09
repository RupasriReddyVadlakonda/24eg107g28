import { useCallback, useMemo, useState } from 'react';
import { Button } from '../../components/ui/Button';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { PageHeader } from '../../components/ui/PageHeader';
import { useResource } from '../../hooks/useResource';
import { auditService } from '../../services/auditService';
import type { AccessLog } from '../../types/api';
import { formatDateTime, labelForDataType } from '../../utils/format';

export function AdminAuditPage() {
  const [page, setPage] = useState(0);
  const load = useCallback(() => auditService.adminPage(page, 100), [page]);
  const { data, loading, error, reload } = useResource(load);
  const columns = useMemo<Column<AccessLog>[]>(() => [
    { key: 'time', header: 'Timestamp', render: (row) => <span className="whitespace-nowrap font-mono text-xs text-vault-secondary">{formatDateTime(row.timestamp)}</span> },
    { key: 'user', header: 'User ID', render: (row) => <span className="font-mono text-xs">{row.userId}</span> },
    { key: 'app', header: 'Application', render: (row) => row.applicationName || 'Unknown application' },
    { key: 'data', header: 'Data type', render: (row) => labelForDataType(row.dataType) },
    { key: 'operation', header: 'Operation', render: (row) => row.operation || '—' },
    { key: 'purpose', header: 'Purpose', render: (row) => <span className="text-vault-secondary">{row.purpose || '—'}</span> },
    { key: 'result', header: 'Result', render: (row) => <StatusBadge>{row.success ? 'SUCCESS' : 'FAILURE'}</StatusBadge> },
    { key: 'ip', header: 'IP address', render: (row) => <span className="font-mono text-xs text-vault-secondary">{row.ipAddress || '—'}</span> },
  ], []);
  return <>
    <PageHeader title="All Audit Logs" description="System-wide protected data access attempts. Sensitive values are not included in this ledger." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading system audit records" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !data?.records.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No system audit records" detail="The admin audit endpoint returned an empty page." /></div>
      : <><p className="mb-3 text-xs text-vault-secondary">Page {page + 1} · {data.records.length} records returned. The backend response does not include a total count.</p><div className="hidden xl:block"><DataTable caption="System-wide access log records" rows={data.records} columns={columns} /></div><div className="space-y-3 xl:hidden">{data.records.map((row) => <article key={row.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4"><div className="flex items-start justify-between gap-2"><div><h2 className="font-semibold">{row.applicationName || 'Unknown application'}</h2><p className="mt-1 text-xs text-vault-secondary">{formatDateTime(row.timestamp)} · User {row.userId}</p></div><StatusBadge>{row.success ? 'SUCCESS' : 'FAILURE'}</StatusBadge></div><p className="mt-3 text-sm">{labelForDataType(row.dataType)} · {row.operation || '—'}</p><p className="mt-1 text-xs text-vault-secondary">{row.purpose || 'Purpose unavailable'} · {row.ipAddress || 'IP unavailable'}</p></article>)}</div><div className="mt-5 flex justify-end gap-2"><Button variant="outline" disabled={page === 0} onClick={() => setPage((value) => Math.max(0, value - 1))}>Previous</Button><Button variant="outline" disabled={!data.hasMore} onClick={() => setPage((value) => value + 1)}>Next</Button></div></>}
  </>;
}
