import { useCallback, useMemo } from 'react';
import { PageHeader } from '../../components/ui/PageHeader';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { useResource } from '../../hooks/useResource';
import { securityService } from '../../services/securityService';
import type { UserRecord } from '../../types/api';
import { formatDate } from '../../utils/format';

export function AdminUsersPage() {
  const load = useCallback(() => securityService.adminUsers(), []);
  const { data, loading, error, reload } = useResource(load);
  const columns = useMemo<Column<UserRecord>[]>(() => [
    { key: 'name', header: 'Name', render: (user) => <span className="font-semibold">{user.fullName}</span> },
    { key: 'email', header: 'Email', render: (user) => user.email },
    { key: 'role', header: 'Role', render: (user) => user.role },
    { key: 'status', header: 'Account status', render: (user) => <StatusBadge>{user.enabled ? 'ACTIVE' : 'DISABLED'}</StatusBadge> },
    { key: 'created', header: 'Created', render: (user) => <span className="text-xs text-vault-secondary">{formatDate(user.createdAt)}</span> },
  ], []);
  return <>
    <PageHeader title="Users" description="User accounts returned by the protected administration API." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading users" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !data?.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No user records" detail="The admin users endpoint returned no accounts." /></div>
      : <><div className="hidden md:block"><DataTable caption="System user accounts" rows={data} columns={columns} /></div><div className="space-y-3 md:hidden">{data.map((user) => <article key={user.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4"><div className="flex items-start justify-between"><div><h2 className="font-semibold">{user.fullName}</h2><p className="mt-1 text-sm text-vault-secondary">{user.email}</p></div><StatusBadge>{user.role}</StatusBadge></div><p className="mt-3 text-xs text-vault-secondary">Created {formatDate(user.createdAt)}</p></article>)}</div></>}
  </>;
}
