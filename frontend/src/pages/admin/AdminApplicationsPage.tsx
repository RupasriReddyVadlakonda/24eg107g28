import { useCallback, useMemo, useState } from 'react';
import { Blocks, Power } from 'lucide-react';
import { PageHeader } from '../../components/ui/PageHeader';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { Button } from '../../components/ui/Button';
import { ConfirmDialog } from '../../components/ui/Dialog';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { useResource } from '../../hooks/useResource';
import { securityService } from '../../services/securityService';
import { useToast } from '../../hooks/useToast';
import type { Consent, ThirdPartyApplication } from '../../types/api';
import { formatDate } from '../../utils/format';

interface AdminAppsData {
  apps: ThirdPartyApplication[];
  consents: Consent[];
}

export function AdminApplicationsPage() {
  const load = useCallback(async (): Promise<AdminAppsData> => {
    const [apps, consents] = await Promise.all([
      securityService.adminApps(),
      securityService.adminConsents().catch(() => [] as Consent[]),
    ]);
    return { apps, consents };
  }, []);
  const { data, loading, error, reload } = useResource(load);
  const apps = data?.apps;
  const consentCounts = useMemo(() => {
    const map = new Map<number, number>();
    for (const item of data?.consents ?? []) {
      map.set(item.applicationId, (map.get(item.applicationId) ?? 0) + 1);
    }
    return map;
  }, [data?.consents]);
  const { showToast } = useToast();
  const [target, setTarget] = useState<ThirdPartyApplication | null>(null);
  const [busy, setBusy] = useState(false);
  const disable = async () => {
    if (!target) return;
    setBusy(true);
    try { await securityService.disableAdminApp(target.id); setTarget(null); showToast('Application disabled.', 'success'); await reload(); }
    catch (reason) { showToast(reason instanceof Error ? reason.message : 'Could not disable the application.', 'error'); }
    finally { setBusy(false); }
  };
  const columns = useMemo<Column<ThirdPartyApplication>[]>(() => [
    { key: 'name', header: 'Application', render: (app) => <div><span className="font-semibold">{app.applicationName}</span><p className="mt-1 text-xs text-vault-secondary">{app.description || 'No description provided'}</p></div> },
    { key: 'client', header: 'Client ID', render: (app) => <span className="font-mono text-xs text-vault-secondary">{app.clientId}</span> },
    { key: 'consents', header: 'Consent count', render: (app) => <span className="text-xs font-semibold text-vault-lit">{consentCounts.get(app.id) ?? 0}</span> },
    { key: 'created', header: 'Created', render: (app) => formatDate(app.createdAt) },
    { key: 'status', header: 'Status', render: (app) => <StatusBadge>{app.active ? 'ACTIVE' : 'DISABLED'}</StatusBadge> },
    { key: 'action', header: 'Action', render: (app) => app.active ? <Button variant="quiet" className="min-h-8 px-2 text-xs" onClick={() => setTarget(app)} icon={<Power size={14} />}>Disable</Button> : '—' },
  ], [consentCounts]);
  return <>
    <PageHeader title="Applications" description="System-wide registered third-party applications." />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading applications" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !apps?.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No registered applications" detail="The admin applications endpoint returned no records." /></div>
      : <><p className="mb-3 flex items-center gap-2 text-xs text-vault-secondary"><Blocks size={15} />{apps.length} application records</p><div className="hidden md:block"><DataTable caption="System third-party applications" rows={apps} columns={columns} /></div><div className="space-y-3 md:hidden">{apps.map((app) => <article key={app.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4"><div className="flex items-start justify-between gap-2"><h2 className="font-semibold">{app.applicationName}</h2><StatusBadge>{app.active ? 'ACTIVE' : 'DISABLED'}</StatusBadge></div><p className="mt-2 text-sm text-vault-secondary">{app.description || 'No description provided'}</p><p className="mt-3 font-mono text-xs text-vault-secondary">{app.clientId}</p><div className="mt-2 flex items-center justify-between text-xs text-vault-secondary"><span>{consentCounts.get(app.id) ?? 0} consents</span>{app.active && <Button variant="quiet" className="px-0" onClick={() => setTarget(app)} icon={<Power size={14} />}>Disable application</Button>}</div></article>)}</div></>}
    <ConfirmDialog open={!!target} title="Disable application?" description={`${target?.applicationName ?? 'This application'} will no longer be able to obtain app tokens. Existing consent records are retained.`} confirmLabel="Disable application" danger busy={busy} onConfirm={() => void disable()} onCancel={() => setTarget(null)} />
  </>;
}
