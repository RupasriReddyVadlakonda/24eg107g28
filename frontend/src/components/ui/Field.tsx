import type { InputHTMLAttributes, ReactNode, SelectHTMLAttributes, TextareaHTMLAttributes } from 'react';

interface BaseProps { label: string; id: string; error?: string; hint?: string; leading?: ReactNode; }
type InputProps = BaseProps & InputHTMLAttributes<HTMLInputElement>;
type SelectProps = BaseProps & SelectHTMLAttributes<HTMLSelectElement>;
type TextAreaProps = BaseProps & TextareaHTMLAttributes<HTMLTextAreaElement>;
const inputClass = 'w-full rounded-lg bg-vault-nav px-3.5 py-3 text-vault-lit placeholder:text-vault-olive border-0 shadow-[inset_0_-2px_0_#25100c] focus-visible:outline focus-visible:outline-2 focus-visible:outline-vault-secondary disabled:opacity-60';

function Description({ id, error, hint }: Pick<BaseProps, 'id' | 'error' | 'hint'>) {
  const description = error || hint;
  return description ? <p id={`${id}-description`} className={`mt-1.5 text-xs ${error ? 'text-[#F0A497]' : 'text-vault-secondary'}`}>
    {error ? <span role="alert">{error}</span> : hint}
  </p> : null;
}

export function TextField({ label, id, error, hint, leading, className = '', ...props }: InputProps) {
  return <div className="min-w-0">
    <label htmlFor={id} className="mb-1.5 block text-sm font-semibold text-vault-lit">{label}</label>
    <div className="relative">
      {leading && <span className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-vault-secondary">{leading}</span>}
      <input id={id} aria-invalid={!!error} aria-describedby={error || hint ? `${id}-description` : undefined} className={`${inputClass} ${leading ? 'pl-10' : ''} ${className}`} {...props} />
    </div>
    <Description id={id} error={error} hint={hint} />
  </div>;
}

export function SelectField({ label, id, error, hint, className = '', children, ...props }: SelectProps) {
  return <div className="min-w-0">
    <label htmlFor={id} className="mb-1.5 block text-sm font-semibold text-vault-lit">{label}</label>
    <select id={id} aria-invalid={!!error} aria-describedby={error || hint ? `${id}-description` : undefined} className={`${inputClass} ${className}`} {...props}>{children}</select>
    <Description id={id} error={error} hint={hint} />
  </div>;
}

export function TextAreaField({ label, id, error, hint, className = '', ...props }: TextAreaProps) {
  return <div className="min-w-0">
    <label htmlFor={id} className="mb-1.5 block text-sm font-semibold text-vault-lit">{label}</label>
    <textarea id={id} aria-invalid={!!error} aria-describedby={error || hint ? `${id}-description` : undefined} className={`${inputClass} min-h-24 resize-y ${className}`} {...props} />
    <Description id={id} error={error} hint={hint} />
  </div>;
}
