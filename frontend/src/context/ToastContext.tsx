import { useCallback, useMemo, useState, type ReactNode } from 'react';
import { AlertTriangle, Check, Info, X } from 'lucide-react';
import { ToastContext, type ToastApi } from './toast-context';

type ToastKind = 'success' | 'error' | 'info';
interface ToastItem { id: number; message: string; kind: ToastKind }
let nextToastId = 0;

export function ToastProvider({ children }: { children: ReactNode }) {
  const [toasts, setToasts] = useState<ToastItem[]>([]);
  const dismiss = useCallback((id: number) => setToasts((items) => items.filter((item) => item.id !== id)), []);
  const showToast = useCallback((message: string, kind: ToastKind = 'info') => {
    const id = ++nextToastId;
    setToasts((items) => [...items.slice(-3), { id, message, kind }]);
    window.setTimeout(() => dismiss(id), kind === 'error' ? 6000 : 4200);
  }, [dismiss]);
  const value = useMemo<ToastApi>(() => ({ showToast }), [showToast]);

  return <ToastContext.Provider value={value}>
    {children}
    <div className="fixed right-4 top-4 z-[100] flex w-[min(24rem,calc(100vw-2rem))] flex-col gap-2" aria-live="polite" aria-relevant="additions">
      {toasts.map((toast) => {
        const Icon = toast.kind === 'success' ? Check : toast.kind === 'error' ? AlertTriangle : Info;
        const surface = toast.kind === 'error' ? 'border-vault-alert' : toast.kind === 'success' ? 'border-vault-copper' : 'border-vault-keyline';
        return <div key={toast.id} role={toast.kind === 'error' ? 'alert' : 'status'} className={`flex items-start gap-3 border ${surface} rounded-lg bg-vault-well px-4 py-3 text-sm text-vault-lit`}>
          <Icon aria-hidden="true" size={18} className="mt-0.5 shrink-0 text-vault-secondary" />
          <p className="min-w-0 flex-1">{toast.message}</p>
          <button type="button" aria-label="Dismiss notification" onClick={() => dismiss(toast.id)} className="rounded p-1 text-vault-secondary hover:text-vault-lit"><X size={16} /></button>
        </div>;
      })}
    </div>
  </ToastContext.Provider>;
}
