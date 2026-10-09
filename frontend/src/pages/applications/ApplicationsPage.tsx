import { useCallback, useMemo, useState } from 'react';
import { Blocks, Plus, Pencil, Power } from 'lucide-react';
import type { AppRegistrationRequest, AppRegistrationResponse, Consent, ThirdPartyApplication } from '../../types/api';
import { applicationService } from '../../services/applicationService';
import { consentService } from '../../services/consentService';
import { useResource } from '../../hooks/useResource';
import { useToast } from '../../hooks/useToast';
import { Button } from '../../components/ui/Button';
import { ConfirmDialog, Dialog } from '../../components/ui/Dialog';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { PageHeader } from '../../components/ui/PageHeader';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { ApplicationForm } from '../../components/applications/ApplicationForm';
import { ClientSecretDialog } from '../../components/applications/ClientSecretDialog';
import { formatDate } from '../../utils/format';

interface ApplicationsData {
  apps: ThirdPartyApplication[];
  consents: Consent[];
}

export function ApplicationsPage() {
  const load = useCallback(async (): Promise<ApplicationsData> => {
    const [apps, consents] = await Promise.all([
      applicationService.list(),
      consentService.list().catch(() => [] as Consent[]),
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
  const [formOpen, setFormOpen] = useState(false);
  const [editing, setEditing] = useState<ThirdPartyApplication | undefined>();
  const [secretResult, setSecretResult] = useState<AppRegistrationResponse | null>(null);
  const [disableTarget, setDisableTarget] = useState<ThirdPartyApplication | null>(null);
  const [busy, setBusy] = useState(false);

  const save = async (payload: AppRegistrationRequest) => {
    setBusy(true);
    try {
      if (editing) {
        await applicationService.update(editing.id, payload);
        showToast('Application details updated.', 'success');
        setFormOpen(false);
        setEditing(undefined);
        await reload();
      } else {
        const result = await applicationService.create(payload);
        setFormOpen(false);
        setSecretResult(result);
        await reload();
      }
    } catch (reason) { showToast(reason instanceof Error ? reason.message : 'Could not save the application.', 'error'); }
    finally { setBusy(false); }
  };

  const disable = async () => {
    if (!disableTarget) return;
    setBusy(true);
    try {
      await applicationService.disable(disableTarget.id);
      setDisableTarget(null);
      showToast('Application disabled.', 'success');
      await reload();
    } catch (reason) { showToast(reason instanceof Error ? reason.message : 'Could not disable this application.', 'error'); }
    finally { setBusy(false); }
  };

  const columns = useMemo<Column<ThirdPartyApplication>[]>(() => [
    { key: 'name', header: 'Application', render: (app) => <div><p className="font-semibold">{app.applicationName}</p><p className="mt-1 max-w-xs truncate text-xs text-vault-secondary">{app.description || 'No description provided'}</p></div> },
    { key: 'client', header: 'Client ID', render: (app) => <span className="font-mono text-xs text-vault-secondary">{app.clientId}</span> },
    { key: 'redirect', header: 'Redirect URI', render: (app) => <span className="block max-w-[16rem] truncate text-xs text-vault-secondary">{app.redirectUri}</span> },
    { key: 'consents', header: 'Consent count', render: (app) => <span className="text-xs font-semibold text-vault-lit">{consentCounts.get(app.id) ?? 0}</span> },
    { key: 'created', header: 'Registered', render: (app) => <span className="whitespace-nowrap text-xs text-vault-secondary">{formatDate(app.createdAt)}</span> },
    { key: 'status', header: 'Status', render: (app) => <StatusBadge>{app.active ? 'ACTIVE' : 'DISABLED'}</StatusBadge> },
    { key: 'actions', header: 'Actions', render: (app) => <div className="flex gap-1"><button type="button" aria-label={`Edit ${app.applicationName}`} disabled={!app.active} onClick={() => { setEditing(app); setFormOpen(true); }} className="rounded p-2 text-vault-secondary hover:bg-white/5 disabled:opacity-40"><Pencil size={16} /></button>{app.active && <button type="button" aria-label={`Disable ${app.applicationName}`} onClick={() => setDisableTarget(app)} className="rounded p-2 text-vault-secondary hover:bg-vault-alert/20 hover:text-[#F0A497]"><Power size={16} /></button>}</div> },
  ], [consentCounts]);

  return <>
    <PageHeader title="Applications" description="Review the third-party applications registered to your account." action={<Button icon={<Plus size={17} />} onClick={() => { setEditing(undefined); setFormOpen(true); }}>Register application</Button>} />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading registered applications" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !apps?.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No applications registered" detail="Register an application to manage its credentials and review future permission requests." action={<Button icon={<Plus size={16} />} onClick={() => setFormOpen(true)}>Register application</Button>} /></div>
      : <><div className="hidden md:block"><DataTable caption="Applications registered to your account" columns={columns} rows={apps} /></div>
        <div className="space-y-3 md:hidden">{apps.map((app) => <article key={app.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4"><div className="flex items-start justify-between gap-3"><div><h2 className="font-semibold">{app.applicationName}</h2><p className="mt-1 text-sm text-vault-secondary">{app.description || 'No description provided'}</p></div><StatusBadge>{app.active ? 'ACTIVE' : 'DISABLED'}</StatusBadge></div><p className="mt-4 break-all font-mono text-xs text-vault-secondary">{app.clientId}</p><div className="mt-3 flex items-center justify-between"><div className="flex flex-col text-xs text-vault-secondary"><span>Registered {formatDate(app.createdAt)}</span><span className="font-semibold text-vault-lit">{consentCounts.get(app.id) ?? 0} consent{(consentCounts.get(app.id) ?? 0) === 1 ? '' : 's'}</span></div><div className="flex gap-2"><button type="button" aria-label={`Edit ${app.applicationName}`} disabled={!app.active} onClick={() => { setEditing(app); setFormOpen(true); }} className="p-2 text-vault-secondary"><Pencil size={16} /></button>{app.active && <button type="button" aria-label={`Disable ${app.applicationName}`} onClick={() => setDisableTarget(app)} className="p-2 text-vault-secondary"><Power size={16} /></button>}</div></div></article>)}</div></>}
    <Dialog open={formOpen} title={editing ? 'Edit application' : 'Register an application'} description="An application receives access only after you approve a matching consent request." onClose={() => { setFormOpen(false); setEditing(undefined); }}>
      <ApplicationForm key={editing?.id ?? 'new'} application={editing} onSave={save} onCancel={() => { setFormOpen(false); setEditing(undefined); }} busy={busy} />
    </Dialog>
    <ClientSecretDialog result={secretResult} onClose={() => { setSecretResult(null); showToast('Client secret cleared from this screen.', 'info'); }} />
    <ConfirmDialog open={!!disableTarget} title="Disable this application?" description={`${disableTarget?.applicationName ?? 'This application'} will no longer be able to obtain application tokens. Existing consent records remain in your history.`} confirmLabel="Disable application" danger busy={busy} onConfirm={() => void disable()} onCancel={() => setDisableTarget(null)} />
    {!!apps?.length && <p className="mt-4 flex items-center gap-2 text-xs text-vault-secondary"><Blocks size={15} />{apps.length} registered application{apps.length === 1 ? '' : 's'}</p>}
  </>;
}
