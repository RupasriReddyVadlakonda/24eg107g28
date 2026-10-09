import type { ButtonHTMLAttributes, ReactNode } from 'react';

type Variant = 'primary' | 'outline' | 'quiet' | 'danger';
interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> { variant?: Variant; icon?: ReactNode; }
const variants: Record<Variant, string> = {
  primary: 'bg-vault-copper text-vault-nav hover:bg-vault-secondary font-bold',
  outline: 'border border-vault-keyline text-vault-lit hover:bg-white/5',
  quiet: 'text-vault-secondary hover:bg-white/5 hover:text-vault-lit',
  danger: 'bg-vault-alert text-vault-lit hover:brightness-110 font-bold',
};

export function Button({ variant = 'primary', icon, className = '', children, ...props }: ButtonProps) {
  return <button {...props} className={`inline-flex min-h-10 items-center justify-center gap-2 rounded-lg px-4 py-2 text-sm transition-colors disabled:opacity-50 ${variants[variant]} ${className}`}>
    {icon}{children}
  </button>;
}
