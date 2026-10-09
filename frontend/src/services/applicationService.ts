import type { AppRegistrationRequest, AppRegistrationResponse, AppUpdateRequest, ThirdPartyApplication } from '../types/api';
import { apiDelete, apiGet, apiPost, apiPut } from './api';

export const applicationService = {
  list: () => apiGet<ThirdPartyApplication[]>('/apps'),
  create: (payload: AppRegistrationRequest) => apiPost<AppRegistrationResponse>('/apps', payload),
  update: (id: number, payload: AppUpdateRequest) => apiPut<ThirdPartyApplication>(`/apps/${id}`, payload),
  disable: (id: number) => apiDelete(`/apps/${id}`),
};
