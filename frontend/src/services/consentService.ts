import type { Consent } from '../types/api';
import { apiGet, apiPost, apiPut } from './api';
import type { ConsentRequest } from '../types/api';

export const consentService = {
  list: () => apiGet<Consent[]>('/consents'),
  get: (id: number) => apiGet<Consent>(`/consents/${id}`),
  grant: (id: number) => apiPut<Consent>(`/consents/${id}/grant`),
  deny: (id: number) => apiPut<Consent>(`/consents/${id}/deny`),
  revoke: (id: number) => apiPut<Consent>(`/consents/${id}/revoke`),
  request: (payload: ConsentRequest) => apiPost<Consent>('/consents/request', payload),
};
