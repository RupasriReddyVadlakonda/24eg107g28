import { useState } from 'react';
import { Check, Copy, KeyRound } from 'lucide-react';
import { Dialog } from '../ui/Dialog';
import { Button } from '../ui/Button';
import type { AppRegistrationResponse } from '../../types/api';

export function ClientSecretDialog({ result, onClose }: { result: AppRegistrationResponse | null; onClose: () => void }) {
  const [copied, setCopied] = useState(false);
  const copySecret = async () => {
    if (!result?.clientSecret) return;
    try { await navigator.clipboard.writeText(result.clientSecret); setCopied(true); }
    catch { setCopied(false); }
  };
  return <Dialog open={!!result} title="Save your client secret" description="This credential is shown once and cannot be retrieved later." onClose={onClose}>
    {result && <div className="space-y-4">
      <div className="flex items-start gap-3 rounded-lg border border-vault-pending/70 bg-vault-pending/10 p-4 text-sm text-vault-lit"><KeyRound className="mt-0.5 shrink-0 text-vault-secondary" size={19} /><p>Copy the secret to your application’s secure credential store now. Do not paste it into public code or share it.</p></div>
      <dl className="space-y-3 text-sm"><div><dt className="text-vault-secondary">Application</dt><dd className="mt-1 font-semibold">{result.applicationName}</dd></div><div><dt className="text-vault-secondary">Client ID</dt><dd className="mt-1 break-all font-mono text-xs">{result.clientId}</dd></div></dl>
      <div><label htmlFor="one-time-secret" className="mb-1.5 block text-sm font-semibold">Client secret</label><input id="one-time-secret" readOnly value={result.clientSecret} onFocus={(event) => event.currentTarget.select()} className="w-full rounded-lg bg-vault-nav p-3 font-mono text-xs text-vault-lit" /></div>
      <div className="flex justify-end gap-2"><Button variant="outline" onClick={onClose}>Close</Button><Button icon={copied ? <Check size={16} /> : <Copy size={16} />} onClick={() => void copySecret()}>{copied ? 'Copied' : 'Copy secret'}</Button></div>
    </div>}
  </Dialog>;
}
