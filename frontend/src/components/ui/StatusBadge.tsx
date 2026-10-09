import type { ReactNode } from 'react';

const tones: Record<string, string> = {
  GRANTED: 'border-vault-copper/70 text-vault-secondary',
  ACTIVE: 'border-vault-copper/70 text-vault-secondary',
  PENDING: 'border-vault-pending/70 text-[#E6D783]',
  DENIED: 'border-vault-alert/70 text-[#F0A497]',
  REVOKED: 'border-vault-olive text-vault-secondary',
  EXPIRED: 'border-vault-olive text-vault-secondary',
  HIGH: 'border-vault-alert/70 text-[#F0A497]',
  CRITICAL: 'border-vault-alert text-[#F0A497]',
  MEDIUM: 'border-vault-pending/70 text-[#E6D783]',
  LOW: 'border-vault-olive text-vault-secondary',
  SUCCESS: 'border-vault-copper/70 text-vault-secondary',
  FAILURE: 'border-vault-alert/70 text-[#F0A497]',
};

export function StatusBadge({ children, className = '' }: { children: ReactNode; className?: string }) {
  const value = typeof children === 'string' ? children.toUpperCase() : '';
  return <span className={`inline-flex items-center gap-1.5 rounded-md border px-2 py-1 text-xs font-bold tracking-wide ${tones[value] ?? 'border-vault-keyline text-vault-secondary'} ${className}`}>
    <span aria-hidden="true" className={`size-1.5 rounded-full ${value === 'GRANTED' || value === 'ACTIVE' || value === 'SUCCESS' ? 'bg-vault-copper' : value === 'PENDING' || value === 'MEDIUM' ? 'bg-vault-pending' : value === 'HIGH' || value === 'CRITICAL' || value === 'FAILURE' || value === 'DENIED' ? 'bg-vault-alert' : 'bg-vault-olive'}`} />
    {children}
  </span>;
}
