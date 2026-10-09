import { useEffect, useId, useRef, type ReactNode } from 'react';
import { X } from 'lucide-react';

interface DialogProps {
  open: boolean;
  title: string;
  description?: string;
  onClose: () => void;
  children: ReactNode;
  labelledBy?: string;
}

export function Dialog({ open, title, description, onClose, children, labelledBy }: DialogProps) {
  const ref = useRef<HTMLDialogElement>(null);
  const generatedId = useId();
  const titleId = labelledBy ?? `vault-dialog-title-${generatedId}`;

  useEffect(() => {
    const dialog = ref.current;
    if (!dialog) return;
    if (open && !dialog.open) dialog.showModal();
    if (!open && dialog.open) dialog.close();
  }, [open]);

  return <dialog ref={ref} className="vault-dialog m-auto w-[min(34rem,calc(100vw-2rem))] rounded-xl border border-vault-keyline bg-vault-well p-0 text-vault-lit backdrop:bg-black/70" onClose={onClose}>
    <div className="flex items-start justify-between gap-4 border-b border-vault-keyline px-5 py-4 sm:px-6">
      <div><h2 id={titleId} className="font-display text-xl">{title}</h2>{description && <p className="mt-1 text-sm text-vault-secondary">{description}</p>}</div>
      <button type="button" aria-label="Close dialog" onClick={() => ref.current?.close()} className="rounded-lg p-2 text-vault-secondary hover:bg-white/5 hover:text-vault-lit"><X size={18} /></button>
    </div>
    <div className="max-h-[75vh] overflow-y-auto px-5 py-5 sm:px-6">{children}</div>
  </dialog>;
}

interface ConfirmDialogProps {
  open: boolean;
  title: string;
  description: string;
  confirmLabel: string;
  danger?: boolean;
  busy?: boolean;
  onConfirm: () => void;
  onCancel: () => void;
}

export function ConfirmDialog({ open, title, description, confirmLabel, danger, busy, onConfirm, onCancel }: ConfirmDialogProps) {
  const ref = useRef<HTMLDialogElement>(null);
  const titleId = useId();
  useEffect(() => {
    if (open && ref.current && !ref.current.open) ref.current.showModal();
    if (!open && ref.current?.open) ref.current.close();
  }, [open]);

  return <dialog ref={ref} className="vault-dialog m-auto w-[min(30rem,calc(100vw-2rem))] rounded-xl border border-vault-keyline bg-vault-well p-0 text-vault-lit backdrop:bg-black/70" onClose={onCancel} aria-labelledby={`vault-confirm-title-${titleId}`}>
    <div className="p-5 sm:p-6">
      <h2 id={`vault-confirm-title-${titleId}`} className="font-display text-xl">{title}</h2>
      <p className="mt-3 text-sm leading-6 text-vault-secondary">{description}</p>
      <div className="mt-6 flex flex-col-reverse justify-end gap-2 sm:flex-row">
        <button type="button" disabled={busy} onClick={() => ref.current?.close()} className="min-h-10 rounded-lg border border-vault-keyline px-4 py-2 text-sm hover:bg-white/5 disabled:opacity-50">Cancel</button>
        <button type="button" disabled={busy} onClick={onConfirm} className={`min-h-10 rounded-lg px-4 py-2 text-sm font-bold disabled:opacity-50 ${danger ? 'bg-vault-alert text-vault-lit' : 'bg-vault-copper text-vault-nav'}`}>
          {busy ? 'Please wait…' : confirmLabel}
        </button>
      </div>
    </div>
  </dialog>;
}
