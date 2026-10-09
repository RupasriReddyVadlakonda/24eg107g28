import { useCallback, useMemo, useState } from 'react';
import { Database, Plus, Pencil, Trash2 } from 'lucide-react';
import { Button } from '../../components/ui/Button';
import { ConfirmDialog, Dialog } from '../../components/ui/Dialog';
import { PageHeader } from '../../components/ui/PageHeader';
import { DataTable, type Column } from '../../components/ui/DataTable';
import { EmptyState, ErrorState, LoadingState } from '../../components/ui/StateView';
import { RevealValue } from '../../components/vault/RevealValue';
import { DataRecordForm } from '../../components/vault/DataRecordForm';
import { useResource } from '../../hooks/useResource';
import { vaultService } from '../../services/vaultService';
import type { DataType, PersonalData, PersonalDataRequest } from '../../types/api';
import { labelForDataType, formatDate } from '../../utils/format';
import { useToast } from '../../hooks/useToast';

export function VaultPage() {
  const loadRecords = useCallback(() => vaultService.list(), []);
  const { data: records, loading, error, reload } = useResource(loadRecords);
  const { showToast } = useToast();
  const [editing, setEditing] = useState<PersonalData | undefined>();
  const [formOpen, setFormOpen] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<PersonalData | null>(null);
  const [saving, setSaving] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const save = async (payload: PersonalDataRequest) => {
    setSaving(true);
    try {
      if (editing) await vaultService.update(editing.id, payload);
      else await vaultService.create(payload);
      setFormOpen(false);
      setEditing(undefined);
      showToast(editing ? 'Vault record updated.' : 'Record added to your vault.', 'success');
      await reload();
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Could not save the vault record.', 'error');
    } finally { setSaving(false); }
  };

  const remove = async () => {
    if (!deleteTarget) return;
    setDeleting(true);
    try {
      await vaultService.remove(deleteTarget.id);
      setDeleteTarget(null);
      showToast(`${labelForDataType(deleteTarget.dataType)} record deleted.`, 'success');
      await reload();
    } catch (reason) {
      showToast(reason instanceof Error ? reason.message : 'Could not delete this record.', 'error');
    } finally { setDeleting(false); }
  };

  const columns = useMemo<Column<PersonalData>[]>(() => [
    { key: 'type', header: 'Data type', render: (row) => <span className="font-semibold">{labelForDataType(row.dataType)}</span> },
    { key: 'value', header: 'Value · masked by default', render: (row) => <RevealValue type={row.dataType as DataType} value={row.value} /> },
    { key: 'description', header: 'Description', render: (row) => <span className="text-vault-secondary">{row.description || '—'}</span> },
    { key: 'updated', header: 'Updated', render: (row) => <span className="whitespace-nowrap text-xs text-vault-secondary">{formatDate(row.updatedAt)}</span> },
    { key: 'actions', header: 'Actions', render: (row) => <div className="flex gap-1">
      <button type="button" aria-label={`Edit ${labelForDataType(row.dataType)}`} onClick={() => { setEditing(row); setFormOpen(true); }} className="rounded p-2 text-vault-secondary hover:bg-white/5 hover:text-vault-lit"><Pencil size={16} /></button>
      <button type="button" aria-label={`Delete ${labelForDataType(row.dataType)}`} onClick={() => setDeleteTarget(row)} className="rounded p-2 text-vault-secondary hover:bg-vault-alert/20 hover:text-[#F0A497]"><Trash2 size={16} /></button>
    </div> },
  ], []);

  return <>
    <PageHeader title="Personal Data Vault" description="Your personal records are encrypted at rest. Values remain masked until you choose to reveal them."
      action={<Button icon={<Plus size={17} />} onClick={() => { setEditing(undefined); setFormOpen(true); }}>Add data</Button>} />
    {loading ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><LoadingState label="Loading encrypted records" /></div>
      : error ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><ErrorState error={error} onRetry={() => void reload()} /></div>
      : !records?.length ? <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No records in your vault" detail="Add a personal data record when you are ready. Data is encrypted before it is stored." action={<Button icon={<Plus size={16} />} onClick={() => { setEditing(undefined); setFormOpen(true); }}>Add your first record</Button>} /></div>
      : <>
        <div className="hidden md:block"><DataTable caption="Personal data stored in your vault" columns={columns} rows={records} /></div>
        <div className="space-y-3 md:hidden">
          {records.map((record) => <article key={record.id} className="rounded-xl border border-vault-keyline bg-vault-well p-4">
            <div className="flex items-start justify-between gap-3"><div><h2 className="font-semibold">{labelForDataType(record.dataType)}</h2><p className="mt-1 text-xs text-vault-secondary">Updated {formatDate(record.updatedAt)}</p></div>
              <div className="flex gap-1"><button type="button" aria-label={`Edit ${labelForDataType(record.dataType)}`} onClick={() => { setEditing(record); setFormOpen(true); }} className="rounded p-2 text-vault-secondary"><Pencil size={16} /></button><button type="button" aria-label={`Delete ${labelForDataType(record.dataType)}`} onClick={() => setDeleteTarget(record)} className="rounded p-2 text-vault-secondary"><Trash2 size={16} /></button></div></div>
            <div className="mt-4 border-t border-vault-keyline pt-3"><RevealValue type={record.dataType as DataType} value={record.value} />{record.description && <p className="mt-2 text-sm text-vault-secondary">{record.description}</p>}</div>
          </article>)}
        </div>
      </>}
    <Dialog open={formOpen} title={editing ? 'Edit vault record' : 'Add to your vault'} description="Personal values are sent only to the secure vault API." onClose={() => { setFormOpen(false); setEditing(undefined); }}>
      <DataRecordForm key={editing?.id ?? 'new'} record={editing} onSave={save} onCancel={() => { setFormOpen(false); setEditing(undefined); }} busy={saving} />
    </Dialog>
    <ConfirmDialog open={!!deleteTarget} title="Delete this vault record?" description={`The ${deleteTarget ? labelForDataType(deleteTarget.dataType) : 'selected'} value will be removed from your vault. This action cannot be undone.`} confirmLabel="Delete record" danger busy={deleting} onConfirm={() => void remove()} onCancel={() => setDeleteTarget(null)} />
    {records?.length ? <p className="mt-4 flex items-center gap-2 text-xs text-vault-secondary"><Database size={15} aria-hidden="true" />{records.length} record{records.length === 1 ? '' : 's'} in your vault</p> : null}
  </>;
}
