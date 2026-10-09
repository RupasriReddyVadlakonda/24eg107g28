import { useState } from 'react';
import { Menu, ShieldCheck } from 'lucide-react';
import { Outlet, useLocation } from 'react-router-dom';
import { Sidebar } from '../components/navigation/Sidebar';
import { useAuth } from '../hooks/useAuth';

const titles: Record<string, string> = {
  '/dashboard': 'Dashboard', '/vault': 'Personal Data Vault', '/consents/requests': 'Consent Requests',
  '/consents': 'Active Consents', '/applications': 'Applications', '/audit': 'Access History',
  '/security-alerts': 'Security Alerts', '/profile': 'Profile', '/admin': 'Admin Dashboard',
  '/admin/users': 'Users', '/admin/applications': 'Applications', '/admin/audit': 'All Audit Logs',
  '/admin/security-alerts': 'Security Alerts',
};

export function AppLayout() {
  const [navOpen, setNavOpen] = useState(false);
  const { user } = useAuth();
  const location = useLocation();
  const title = titles[location.pathname] ?? 'Personal Data Vault';
  const isAdminRoute = location.pathname.startsWith('/admin');

  return <div className="min-h-screen bg-vault-page lg:flex">
    <Sidebar open={navOpen} onClose={() => setNavOpen(false)} />
    <div className="min-w-0 flex-1">
      <header className="sticky top-0 z-30 flex h-[4.5rem] items-center justify-between border-b border-vault-keyline bg-vault-page/95 px-4 backdrop-blur-sm sm:px-7">
        <div className="flex items-center gap-3">
          <button type="button" aria-label="Open navigation" aria-expanded={navOpen} onClick={() => setNavOpen(true)} className="rounded-lg p-2 text-vault-secondary hover:bg-vault-well lg:hidden"><Menu size={20} /></button>
          <span className="font-semibold text-vault-lit">{title}</span>
        </div>
        <div className="flex items-center gap-2 rounded-lg border border-vault-keyline px-3 py-2 text-xs sm:text-sm">
          <ShieldCheck size={16} className="text-vault-secondary" aria-hidden="true" />
          <span className="text-vault-secondary">{isAdminRoute ? 'System administration' : 'Your account'}</span>
          <span className="hidden border-l border-vault-keyline pl-2 text-vault-lit sm:inline">{user?.role}</span>
        </div>
      </header>
      <main className="mx-auto w-full max-w-[1440px] px-4 py-6 sm:px-7 sm:py-8 lg:px-10">
        <Outlet />
      </main>
    </div>
  </div>;
}
