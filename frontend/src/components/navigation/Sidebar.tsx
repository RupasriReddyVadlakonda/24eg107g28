import { NavLink, useNavigate } from 'react-router-dom';
import { Activity, Bell, Blocks, Database, FileClock, FileCheck2, LayoutDashboard, LogOut, ShieldAlert, Users, X, type LucideIcon } from 'lucide-react';
import { useAuth } from '../../hooks/useAuth';
import { useToast } from '../../hooks/useToast';

interface NavItem { label: string; to: string; icon: LucideIcon; end?: boolean }
const accountItems: NavItem[] = [
  { label: 'Dashboard', to: '/dashboard', icon: LayoutDashboard },
  { label: 'Personal Data Vault', to: '/vault', icon: Database },
];
const permissionItems: NavItem[] = [
  { label: 'Consent Requests', to: '/consents/requests', icon: FileCheck2 },
  { label: 'Active Consents', to: '/consents', icon: ShieldAlert },
  { label: 'Applications', to: '/applications', icon: Blocks },
];
const recordItems: NavItem[] = [
  { label: 'Access History', to: '/audit', icon: FileClock },
  { label: 'Security Alerts', to: '/security-alerts', icon: Bell },
];
const adminItems: NavItem[] = [
  { label: 'Admin Dashboard', to: '/admin', icon: LayoutDashboard, end: true },
  { label: 'Users', to: '/admin/users', icon: Users },
  { label: 'Applications', to: '/admin/applications', icon: Blocks },
  { label: 'All Audit Logs', to: '/admin/audit', icon: Activity },
  { label: 'Security Alerts', to: '/admin/security-alerts', icon: ShieldAlert },
];

export function Sidebar({ open, onClose }: { open: boolean; onClose: () => void }) {
  const { user, signOut } = useAuth();
  const { showToast } = useToast();
  const navigate = useNavigate();
  const groups: Array<[string, NavItem[]]> = [
    ['My data', accountItems], ['Permissions', permissionItems], ['Records', recordItems],
    ...(user?.role === 'ADMIN' ? [['Administration', adminItems] as [string, NavItem[]]] : []),
  ];

  const logout = async () => {
    try {
      await signOut();
      showToast('You have signed out.', 'success');
    } catch {
      showToast('Your local session ended, but the server could not confirm token revocation.', 'info');
    } finally { onClose(); navigate('/login', { replace: true }); }
  };

  return <>
    <button type="button" aria-label="Close navigation" onClick={onClose} className={`fixed inset-0 z-40 bg-black/60 transition-opacity lg:hidden ${open ? 'visible opacity-100' : 'invisible opacity-0'}`} />
    <aside className={`fixed inset-y-0 left-0 z-50 flex w-[17rem] flex-col border-r border-vault-keyline bg-vault-nav transition-transform duration-150 lg:sticky lg:top-0 lg:h-screen lg:translate-x-0 ${open ? 'translate-x-0' : '-translate-x-full'}`} aria-label="Main navigation">
      <div className="flex h-[4.5rem] items-center justify-between border-b border-vault-keyline px-5">
        <NavLink to="/dashboard" onClick={onClose} className="flex items-center gap-3">
          <span className="flex size-9 items-center justify-center rounded-lg border border-vault-keyline text-vault-secondary"><Database size={18} /></span>
          <span className="font-display text-base leading-tight text-vault-lit">Personal Data<br />Vault</span>
        </NavLink>
        <button type="button" onClick={onClose} aria-label="Close navigation" className="rounded p-2 text-vault-secondary hover:bg-white/5 lg:hidden"><X size={18} /></button>
      </div>
      <nav className="vault-scrollbar flex-1 overflow-y-auto px-3 py-5">
        {groups.map(([group, items]) => <section key={group} className="mb-6">
          <h2 className="mb-2 px-3 text-xs font-bold text-vault-olive">{group}</h2>
          <ul className="space-y-1">
            {items.map(({ label, to, icon: Icon, end }) => <li key={to}>
              <NavLink to={to} end={end} onClick={onClose} className={({ isActive }) => `flex min-h-10 items-center gap-3 rounded-lg px-3 py-2 text-sm transition-colors ${isActive ? 'bg-vault-well text-vault-lit' : 'text-vault-secondary hover:bg-vault-well/70 hover:text-vault-lit'}`}>
                {({ isActive }) => <><Icon size={18} strokeWidth={isActive ? 2 : 1.7} aria-hidden="true" /><span>{label}</span></>}
              </NavLink>
            </li>)}
          </ul>
        </section>)}
        <section className="mb-6">
          <h2 className="mb-2 px-3 text-xs font-bold text-vault-olive">Account</h2>
          <NavLink to="/profile" onClick={onClose} className={({ isActive }) => `flex min-h-10 items-center gap-3 rounded-lg px-3 py-2 text-sm transition-colors ${isActive ? 'bg-vault-well text-vault-lit' : 'text-vault-secondary hover:bg-vault-well/70 hover:text-vault-lit'}`}><Users size={18} /><span>Profile</span></NavLink>
          <button type="button" onClick={logout} className="mt-1 flex min-h-10 w-full items-center gap-3 rounded-lg px-3 py-2 text-left text-sm text-vault-secondary transition-colors hover:bg-vault-well/70 hover:text-vault-lit"><LogOut size={18} /><span>Logout</span></button>
        </section>
      </nav>
      <div className="border-t border-vault-keyline px-5 py-4">
        <p className="truncate text-sm font-semibold text-vault-lit">{user?.fullName || user?.email || 'Vault account'}</p>
        <p className="mt-0.5 text-xs text-vault-secondary">{user?.role === 'ADMIN' ? 'System administrator' : 'Personal account'}</p>
      </div>
    </aside>
  </>;
}
