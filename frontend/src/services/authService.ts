import type { AuthResponse, LoginRequest, RegisterRequest, UserRecord } from '../types/api';
import { api, apiGet, apiPost } from './api';
import { clearSession, writeSession } from './sessionStore';

export const login = (request: LoginRequest) => apiPost<AuthResponse>('/auth/login', request);
export const register = (request: RegisterRequest) => apiPost<AuthResponse>('/auth/register', request);
export const refreshSession = (refreshToken: string) => apiPost<AuthResponse>('/auth/refresh', { refreshToken });
export const getProfile = () => apiGet<UserRecord>('/auth/me');
export async function logout(refreshToken: string): Promise<void> {
  try { await api.post('/auth/logout', { refreshToken }); }
  finally { clearSession(); }
}
export { writeSession };
