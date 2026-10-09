import type { Consent, SecurityAlert, ThirdPartyApplication, UserRecord } from '../types/api';
import { apiGet, apiPut } from './api';

export const securityService = {
  alerts: () => apiGet<SecurityAlert[]>('/security-alerts'),
  resolveOwnAlert: (id: number) => apiPut<SecurityAlert>(`/security-alerts/${id}/resolve`),
  adminAlerts: () => apiGet<SecurityAlert[]>('/admin/alerts'),
  resolveAdminAlert: (id: number) => apiPut<SecurityAlert>(`/admin/alerts/${id}/resolve`),
  disableAdminApp: (id: number) => apiPut<void>(`/admin/apps/${id}/disable`),
  adminUsers: () => apiGet<UserRecord[]>('/admin/users'),
  adminApps: () => apiGet<ThirdPartyApplication[]>('/admin/apps'),
  adminConsents: () => apiGet<Consent[]>('/admin/consents'),
};
