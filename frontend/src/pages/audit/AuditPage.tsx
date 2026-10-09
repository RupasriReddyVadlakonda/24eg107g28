import { useCallback, useMemo, useState } from 'react';
import { MoreHorizontal } from 'lucide-react';
import { PageHeader } from '../../components/ui/PageHeader';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { SelectField, TextField } from '../../components/ui/Field';
import { Button } from '../../components/ui/Button';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { useResource } from '../../hooks/useResource';
import { auditService } from '../../services/auditService';
import type { AccessLog, DataType } from '../../types/api';
import { formatDateTime, labelForDataType } from '../../utils/format';
import { useToast } from '../../hooks/useToast';

interface Filters { app: string; type: string; operation: string; success: string; from: string; to: string }
const initialFilters: Filters = { app: '', type: '', operation: '', success: '', from: '', to: '' };

export function AuditPage() {
  const load = useCallback(() => auditService.ownPage(0, 100), []);
  const { data: firstPage, loading, error, reload, setData } = useResource(load);
  const { showToast } = useToast();
  const [filters, setFilters] = useState<Filters>(initialFilters);
  const [loadingMore, setLoadingMore] = useState(false);
  const rows = useMemo(() => firstPage?.records ?? [], [firstPage?.records]);
  const apps = useMemo(() => [...new Set(rows.map((row) => row.applicationName).filter((name): name is string => !!name))], [rows]);

  const filtered = useMemo(() => rows.filter((row) => {
    const date = new Date(row.timestamp);
    const from = filters.from ? new Date(`${filters.from}T00:00:00`) : null;
    const to = filters.to ? new Date(`${filters.to}T23:59:59.999`) : null;
    return (!filters.app || row.applicationName === filters.app)
      && (!filters.type || row.dataType === filters.type)
      && (!filters.operation || row.operation === filters.operation)
      && (!filters.success || String(row.success) === filters.success)
      && (!from || date >= from) && (!to || date <= to);
  }), [rows, filters]);

  const columns = useMemo<Column<AccessLog>[]>(() => [
    { key: 'time', header: 'Timestamp', render: (row) => <span className="whitespace-nowrap font-mono text-xs text-vault-secondary">{formatDateTime(row.timestamp)}</span> },
    { key: 'application', header: 'Application', render: (row) => row.applicationName || 'Unknown application' },
    { key: 'type', header: 'Data type', render: (row) => labelForDataType(row.dataType) },
    { key: 'operation', header: 'Operation', render: (row) => row.operation || '—' },
    { key: 'purpose', header: 'Purpose', render: (row) => <span className="text-vault-secondary">{row.purpose || '—'}</span> },
    { key: 'status', header: 'Result', render: (row) => <StatusBadge>{row.success ? 'SUCCESS' : 'FAILURE'}</StatusBadge> },
    { key: 'ip', header: 'IP address', render: (row) => <span className="font-mono text-xs text-vault-secondary">{row.ipAddress || '—'}</span> },
  ], []);

  const loadMore = async () => {
    if (!firstPage) return;
    setLoadingMore(true);
    try {
      const next = await auditService.ownPage(firstPage.page + 1, 100);
      setData({ records: [...firstPage.records, ...next.records], hasMore: next.hasMore, page: next.page });
    } catch (reason) { showToast(reason instanceof Error ? reason.message : 'Unable to load more audit records.', 'error'); }
    finally { setLoadingMore(false); }
  };
  const setFilter = (key: keyof Filters, value: string) => setFilters((current) => ({ ...current, [key]: value }));

  return <>
    <PageHeader title="Access History" description="Review attempts to access your data. This ledger never includes the personal values themselves." />
    <section className="mb-5 grid gap-3 rounded-xl border border-vault-keyline bg-vault-well p-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6" aria-label="Filter audit records">
      <SelectField id="filter-app" label="Application" value={filters.app} onChange={(event) => setFilter('app', event.target.value)}><option value="">All applications</option>{apps.map((app) => <option key={app}>{app}</option>)}</SelectField>
      <SelectField id="filter-type" label="Data type" value={filters.type} onChange={(event) => setFilter('type', event.target.value)}><option value="">All data types</option>{(['NAME','EMAIL','PHONE','ADDRESS','DATE_OF_BIRTH','NATIONAL_ID','PASSPORT','FINANCIAL_INFORMATION','HEALTH_INFORMATION','CUSTOM'] as DataType[]).map((type) => <option key={type} value={type}>{labelForDataType(type)}</option>)}</SelectField>
      <SelectField id="filter-operation" label="Operation" value={filters.operation} onChange={(event) => setFilter('operation', event.target.value)}><option value="">All operations</option><option>READ</option><option>WRITE</option><option>READ_WRITE</option></SelectField>
      <SelectField id="filter-success" label="Outcome" value={filters.success} onChange={(event) => setFilter('success', event.target.value)}><option value="">All outcomes</option><option value="true">Success</option><option value="false">Failure</option></SelectField>
      <TextField id="filter-from" label="From" type="date" value={filters.from} onChange={(event) => setFilter('from', event.target.value)} />
      <TextField id="filter-to" label="To" type="date" value={filters.to} onChange={(event) => setFilter('to', event.target.value)} />
      <Button variant="quiet" className="sm:col-span-2 lg:col-span-3 xl:col-span-6 xl:justify-self-end" onClick={() => setFilters(initialFilters)}>Clear filters</Button>
    </section>

    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading access history" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !filtered.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title={rows.length ? 'No records match these filters' : 'No access records found'} detail={rows.length ? 'Adjust or clear the filters to see other entries.' : 'Access records are added whenever an application attempts a protected request.'} /></div>
      : <>
        <p className="mb-3 text-xs text-vault-secondary">Showing {filtered.length} of {rows.length} loaded records. The user API does not provide a total count.</p>
        <div className="hidden lg:block"><DataTable caption="Personal data access history" columns={columns} rows={filtered} /></div>
        <div className="space-y-3 lg:hidden">{filtered.map((row) => <article key={row.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4"><div className="flex items-start justify-between gap-3"><div><h2 className="font-semibold">{row.applicationName || 'Unknown application'}</h2><p className="mt-1 font-mono text-xs text-vault-secondary">{formatDateTime(row.timestamp)}</p></div><StatusBadge>{row.success ? 'SUCCESS' : 'FAILURE'}</StatusBadge></div><dl className="mt-4 grid grid-cols-[auto_1fr] gap-x-4 gap-y-2 text-sm"><dt className="text-vault-secondary">Data / operation</dt><dd>{labelForDataType(row.dataType)} · {row.operation || '—'}</dd><dt className="text-vault-secondary">Purpose</dt><dd>{row.purpose || '—'}</dd><dt className="text-vault-secondary">IP address</dt><dd className="font-mono text-xs">{row.ipAddress || '—'}</dd></dl></article>)}</div>
        {firstPage?.hasMore && <div className="mt-5 flex justify-center"><Button variant="outline" disabled={loadingMore} icon={<MoreHorizontal size={16} />} onClick={() => void loadMore()}>{loadingMore ? 'Loading…' : 'Load next 100 records'}</Button></div>}
      </>}
  </>;
}
