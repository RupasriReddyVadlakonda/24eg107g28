import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ToastProvider } from './context/ToastContext';
import { AppLayout } from './layouts/AppLayout';
import { AuthLayout } from './layouts/AuthLayout';
import { AdminRoute, ProtectedRoute, PublicOnlyRoute } from './routes/guards';
import { LoginPage } from './pages/auth/LoginPage';
import { RegisterPage } from './pages/auth/RegisterPage';
import { DashboardPage } from './pages/dashboard/DashboardPage';
import { VaultPage } from './pages/vault/VaultPage';
import { ConsentRequestsPage } from './pages/consents/ConsentRequestsPage';
import { ConsentsPage } from './pages/consents/ConsentsPage';
import { ApplicationsPage } from './pages/applications/ApplicationsPage';
import { AuditPage } from './pages/audit/AuditPage';
import { SecurityAlertsPage } from './pages/alerts/SecurityAlertsPage';
import { ProfilePage } from './pages/profile/ProfilePage';
import { AdminDashboardPage } from './pages/admin/AdminDashboardPage';
import { AdminUsersPage } from './pages/admin/AdminUsersPage';
import { AdminApplicationsPage } from './pages/admin/AdminApplicationsPage';
import { AdminAuditPage } from './pages/admin/AdminAuditPage';
import { AdminSecurityAlertsPage } from './pages/admin/AdminSecurityAlertsPage';

const router = createBrowserRouter([
  { element: <PublicOnlyRoute />, children: [{ element: <AuthLayout />, children: [
    { path: '/login', element: <LoginPage /> }, { path: '/register', element: <RegisterPage /> },
  ] }] },
  { element: <ProtectedRoute />, children: [{ element: <AppLayout />, children: [
    { path: '/', element: <Navigate to="/dashboard" replace /> },
    { path: '/dashboard', element: <DashboardPage /> },
    { path: '/vault', element: <VaultPage /> },
    { path: '/consents/requests', element: <ConsentRequestsPage /> },
    { path: '/consents', element: <ConsentsPage /> },
    { path: '/applications', element: <ApplicationsPage /> },
    { path: '/audit', element: <AuditPage /> },
    { path: '/security-alerts', element: <SecurityAlertsPage /> },
    { path: '/profile', element: <ProfilePage /> },
    { element: <AdminRoute />, children: [
      { path: '/admin', element: <AdminDashboardPage /> },
      { path: '/admin/users', element: <AdminUsersPage /> },
      { path: '/admin/applications', element: <AdminApplicationsPage /> },
      { path: '/admin/audit', element: <AdminAuditPage /> },
      { path: '/admin/security-alerts', element: <AdminSecurityAlertsPage /> },
    ] },
  ] }] },
  { path: '*', element: <Navigate to="/dashboard" replace /> },
]);

export default function App() {
  return <AuthProvider><ToastProvider><RouterProvider router={router} /></ToastProvider></AuthProvider>;
}
