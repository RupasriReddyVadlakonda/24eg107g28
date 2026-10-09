import { useState } from 'react';
import { Eye, EyeOff } from 'lucide-react';
import type { DataType } from '../../types/api';
import { maskSensitiveValue } from '../../utils/format';

export function RevealValue({ type, value }: { type: DataType; value: string }) {
  const [revealed, setRevealed] = useState(false);
  return <div className="flex min-w-0 items-center gap-2">
    <span className="min-w-0 break-all font-mono text-xs text-vault-lit" aria-live="polite">{revealed ? value : maskSensitiveValue(type, value)}</span>
    <button type="button" aria-label={`${revealed ? 'Hide' : 'Reveal'} ${type.replaceAll('_', ' ').toLowerCase()} value`} aria-pressed={revealed}
      onClick={() => setRevealed((current) => !current)} className="shrink-0 rounded-md p-1.5 text-vault-secondary hover:bg-white/5 hover:text-vault-lit">
      {revealed ? <EyeOff size={16} /> : <Eye size={16} />}
    </button>
  </div>;
}
