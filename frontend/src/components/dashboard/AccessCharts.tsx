import { Bar, BarChart, CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';
import type { AccessLog } from '../../types/api';
import { labelForDataType } from '../../utils/format';
import { EmptyState } from '../ui/StateView';

const tipStyle = { background: '#310E0A', border: '1px solid #5A4135', borderRadius: 8, color: '#FADFC9', fontSize: 12 };
const axis = { fill: '#D99F6C', fontSize: 11 };

function ChartSection({ title, children }: { title: string; children: React.ReactNode }) {
  return <section className="rounded-xl border border-vault-keyline bg-vault-well p-4 sm:p-5">
    <h2 className="mb-4 font-semibold text-vault-lit">{title}</h2>{children}
  </section>;
}

export function AccessCharts({ records }: { records: AccessLog[] }) {
  if (!records.length) return <div className="rounded-xl border border-vault-keyline bg-vault-well"><EmptyState title="No access records to chart" detail="Charts will use audit events recorded by the service. No illustrative data is shown." /></div>;

  const byDay = new Map<string, number>();
  const byApp = new Map<string, number>();
  const byType = new Map<string, number>();
  for (const record of records) {
    const date = new Date(record.timestamp);
    if (!Number.isNaN(date.getTime())) {
      const key = date.toLocaleDateString(undefined, { month: 'short', day: 'numeric' });
      byDay.set(key, (byDay.get(key) ?? 0) + 1);
    }
    const app = record.applicationName || 'Unknown application';
    byApp.set(app, (byApp.get(app) ?? 0) + 1);
    const type = labelForDataType(record.dataType);
    byType.set(type, (byType.get(type) ?? 0) + 1);
  }
  const daily = [...byDay.entries()].map(([date, requests]) => ({ date, requests }));
  const apps = [...byApp.entries()].map(([application, requests]) => ({ application, requests })).sort((a, b) => b.requests - a.requests).slice(0, 6);
  const types = [...byType.entries()].map(([dataType, requests]) => ({ dataType, requests })).sort((a, b) => b.requests - a.requests);

  return <div className="grid gap-4 xl:grid-cols-2">
    <ChartSection title="Access over time">
      <p className="mb-2 text-xs text-vault-secondary">Audit events in the loaded record window</p>
      <div className="h-56 min-w-0" role="img" aria-label="Line chart of real audit events by date">
        <ResponsiveContainer width="100%" height="100%"><LineChart data={daily} margin={{ top: 8, right: 12, left: -16, bottom: 4 }}>
          <CartesianGrid stroke="#5A4135" strokeDasharray="3 5" vertical={false} />
          <XAxis dataKey="date" tick={axis} axisLine={{ stroke: '#7D6C58' }} tickLine={false} />
          <YAxis allowDecimals={false} tick={axis} axisLine={false} tickLine={false} width={40} label={{ value: 'Requests', angle: -90, position: 'insideLeft', fill: '#D99F6C', fontSize: 11 }} />
          <Tooltip contentStyle={tipStyle} />
          <Line type="monotone" dataKey="requests" name="Access events" stroke="#B2813E" strokeWidth={2} dot={{ r: 3, fill: '#B2813E' }} activeDot={{ r: 5 }} />
        </LineChart></ResponsiveContainer>
      </div>
    </ChartSection>
    <ChartSection title="Access by application">
      <div className="h-56 min-w-0" role="img" aria-label="Bar chart of audit event counts by application">
        <ResponsiveContainer width="100%" height="100%"><BarChart data={apps} layout="vertical" margin={{ top: 4, right: 12, left: 4, bottom: 4 }}>
          <CartesianGrid stroke="#5A4135" strokeDasharray="3 5" horizontal={false} />
          <XAxis type="number" allowDecimals={false} tick={axis} axisLine={{ stroke: '#7D6C58' }} tickLine={false} label={{ value: 'Requests', position: 'insideBottomRight', offset: -2, fill: '#D99F6C', fontSize: 11 }} />
          <YAxis type="category" dataKey="application" width={112} tick={axis} axisLine={false} tickLine={false} />
          <Tooltip contentStyle={tipStyle} /><Bar dataKey="requests" name="Access events" fill="#B2813E" radius={[0, 3, 3, 0]} />
        </BarChart></ResponsiveContainer>
      </div>
    </ChartSection>
    <ChartSection title="Access by data type">
      <div className="h-56 min-w-0" role="img" aria-label="Bar chart of audit event counts by data type">
        <ResponsiveContainer width="100%" height="100%"><BarChart data={types} margin={{ top: 4, right: 12, left: -14, bottom: 35 }}>
          <CartesianGrid stroke="#5A4135" strokeDasharray="3 5" vertical={false} />
          <XAxis dataKey="dataType" tick={{ ...axis, angle: -28, textAnchor: 'end' }} interval={0} axisLine={{ stroke: '#7D6C58' }} tickLine={false} />
          <YAxis allowDecimals={false} tick={axis} axisLine={false} tickLine={false} width={38} label={{ value: 'Requests', angle: -90, position: 'insideLeft', fill: '#D99F6C', fontSize: 11 }} />
          <Tooltip contentStyle={tipStyle} /><Legend wrapperStyle={{ color: '#D99F6C', fontSize: 12 }} />
          <Bar dataKey="requests" name="Access events" fill="#D99F6C" radius={[3, 3, 0, 0]} />
        </BarChart></ResponsiveContainer>
      </div>
    </ChartSection>
  </div>;
}
