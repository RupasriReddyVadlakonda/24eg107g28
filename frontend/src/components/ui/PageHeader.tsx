import type { ReactNode } from 'react';

export function PageHeader({ title, description, action }: { title: string; description?: string; action?: ReactNode }) {
  return <div className="mb-7 flex flex-col justify-between gap-4 sm:flex-row sm:items-end">
    <div>
      <h1 className="font-display text-3xl leading-tight text-vault-lit sm:text-4xl">{title}</h1>
      {description && <p className="mt-2 max-w-3xl text-sm text-vault-secondary">{description}</p>}
    </div>
    {action && <div className="shrink-0">{action}</div>}
  </div>;
}
