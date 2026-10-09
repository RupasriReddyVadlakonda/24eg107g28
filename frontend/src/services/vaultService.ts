import type { PersonalData, PersonalDataRequest } from '../types/api';
import { apiDelete, apiGet, apiPost, apiPut } from './api';

export const vaultService = {
  list: () => apiGet<PersonalData[]>('/vault/data'),
  get: (id: number) => apiGet<PersonalData>(`/vault/data/${id}`),
  create: (payload: PersonalDataRequest) => apiPost<PersonalData>('/vault/data', payload),
  update: (id: number, payload: PersonalDataRequest) => apiPut<PersonalData>(`/vault/data/${id}`, payload),
  remove: (id: number) => apiDelete(`/vault/data/${id}`),
};
