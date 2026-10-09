import type { ReactNode } from 'react';

export interface Column<T> { key: string; header: string; render: (row: T) => ReactNode; className?: string; mobile?: boolean }
export function DataTable<T extends { id: number | string }>({ columns, rows, caption }: { columns: Column<T>[]; rows: T[]; caption: string }) {
  return <div className="overflow-x-auto rounded-lg border border-vault-keyline">
    <table className="w-full min-w-[44rem] border-collapse text-left text-sm">
      <caption className="sr-only">{caption}</caption>
      <thead className="bg-vault-nav text-xs font-bold uppercase tracking-wide text-vault-secondary">
        <tr>{columns.map((column) => <th key={column.key} scope="col" className={`px-4 py-3 ${column.className ?? ''}`}>{column.header}</th>)}</tr>
      </thead>
      <tbody className="divide-y divide-vault-keyline/70">
        {rows.map((row) => <tr key={row.id} className="align-top transition-colors hover:bg-white/[0.025]">
          {columns.map((column) => <td key={column.key} className={`px-4 py-3.5 text-vault-lit ${column.className ?? ''}`}>{column.render(row)}</td>)}
        </tr>)}
      </tbody>
    </table>
  </div>;
}
