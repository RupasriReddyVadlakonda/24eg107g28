import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import type { ReactNode } from 'react';
import { AdminRoute, ProtectedRoute } from './guards';

const auth = vi.hoisted(() => ({ user: null as null | { userId: number; email: string; role: 'USER' | 'ADMIN' }, ready: true }));
vi.mock('../hooks/useAuth', () => ({ useAuth: () => ({ ...auth, signIn: vi.fn(), signUp: vi.fn(), signOut: vi.fn() }) }));

function renderRoute(path: string, element: ReactNode) {
  return render(<MemoryRouter initialEntries={[path]}><Routes>
    <Route element={<ProtectedRoute />}><Route path="/vault" element={element} /><Route element={<AdminRoute />}><Route path="/admin" element={<p>Admin page</p>} /></Route></Route>
    <Route path="/login" element={<p>Sign in page</p>} /><Route path="/dashboard" element={<p>Dashboard page</p>} />
  </Routes></MemoryRouter>);
}

describe('route guards', () => {
  it('redirects unauthenticated users to sign in', () => {
    auth.user = null;
    renderRoute('/vault', <p>Vault page</p>);
    expect(screen.getByText('Sign in page')).toBeInTheDocument();
  });

  it('keeps admin routes role-gated', () => {
    auth.user = { userId: 2, email: 'user@example.test', role: 'USER' };
    renderRoute('/admin', <p>Vault page</p>);
    expect(screen.getByText('Dashboard page')).toBeInTheDocument();
    expect(screen.queryByText('Admin page')).not.toBeInTheDocument();
  });
});
