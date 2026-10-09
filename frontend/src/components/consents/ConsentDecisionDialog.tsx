import { useState } from 'react';
import { Check, X } from 'lucide-react';
import type { Consent } from '../../types/api';
import { labelForDataType } from '../../utils/format';
import { ConfirmDialog } from '../ui/Dialog';
import { Button } from '../ui/Button';

export function ConsentDecisionDialog({ consent, onAction, busy }: {
  consent: Consent;
  onAction: (action: 'grant' | 'deny') => Promise<void>;
  busy: boolean;
}) {
  const [confirm, setConfirm] = useState<'grant' | 'deny' | null>(null);
  const actionLabel = confirm === 'grant' ? 'Grant access' : 'Deny request';
  const act = async () => { if (!confirm) return; await onAction(confirm); setConfirm(null); };
  return <>
    <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-end">
      <Button variant="outline" icon={<X size={15} />} onClick={() => setConfirm('deny')}>Deny</Button>
      <Button icon={<Check size={15} />} onClick={() => setConfirm('grant')}>Grant access</Button>
    </div>
    <ConfirmDialog open={confirm !== null} title={`${actionLabel}?`}
      description={`${consent.applicationName} is requesting ${consent.allowedOperation} access to ${labelForDataType(consent.dataType)} for “${consent.purpose}” for ${consent.requestedDurationDays} day${consent.requestedDurationDays === 1 ? '' : 's'}. ${confirm === 'grant' ? 'You can revoke granted consent later.' : 'The application will not receive this permission.'}`}
      confirmLabel={actionLabel} danger={confirm === 'deny'} busy={busy} onConfirm={() => void act()} onCancel={() => setConfirm(null)} />
  </>;
}
