import { AlertCircle, LoaderCircle, RefreshCw } from 'lucide-react';
import { Button } from './Button';
import { toUserMessage } from '../../services/api';

export function LoadingState({ label = 'Loading records' }: { label?: string }) {
  return <div role="status" className="flex min-h-44 flex-col items-center justify-center gap-3 text-sm text-vault-secondary">
    <LoaderCircle size={24} className="animate-spin" aria-hidden="true" />{label}
  </div>;
}

export function EmptyState({ title, detail, action }: { title: string; detail?: string; action?: React.ReactNode }) {
  return <div className="flex min-h-40 flex-col items-center justify-center px-6 py-10 text-center">
    <p className="font-semibold text-vault-lit">{title}</p>
    {detail && <p className="mt-1.5 max-w-lg text-sm text-vault-secondary">{detail}</p>}
    {action && <div className="mt-4">{action}</div>}
  </div>;
}

export function ErrorState({ error, onRetry }: { error: unknown; onRetry: () => void }) {
  const message = toUserMessage(error, 'Unable to load this information. Try again.');
  return <div role="alert" className="flex flex-col items-center justify-center gap-3 py-10 text-center">
    <AlertCircle size={24} className="text-[#F0A497]" aria-hidden="true" />
    <p className="max-w-lg text-sm text-vault-lit">{message}</p>
    <Button variant="outline" icon={<RefreshCw size={16} />} onClick={onRetry}>Try again</Button>
  </div>;
}
