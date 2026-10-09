import axios, { AxiosError, type AxiosRequestConfig, type InternalAxiosRequestConfig } from 'axios';
import type { ApiResponse } from '../types/api';
import { clearSession, getAccessToken, getRefreshToken, updateTokens } from './sessionStore';

interface RetryConfig extends InternalAxiosRequestConfig { _retry?: boolean }
export class ApiError extends Error {
  status?: number;
  errorCode?: string;
  constructor(message: string, status?: number, errorCode?: string) {
    super(message); this.name = 'ApiError'; this.status = status; this.errorCode = errorCode;
  }
}

const configuredApiBaseUrl = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.trim();
if (import.meta.env.PROD && !configuredApiBaseUrl) {
  throw new Error('VITE_API_BASE_URL must be set to the deployed API URL before building for production.');
}

export const apiBaseUrl = (configuredApiBaseUrl || 'http://localhost:8080/api').replace(/\/$/, '');
export const api = axios.create({ baseURL: apiBaseUrl, headers: { Accept: 'application/json', 'Content-Type': 'application/json' }, timeout: 15_000 });

api.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

let refreshInFlight: Promise<string> | null = null;
export function httpErrorMessage(status: number, bodyMessage?: string): string {
  if (status === 403) return 'You do not have permission to perform this action.';
  if (status === 429) return 'Too many requests. Please try again later.';
  const messages: Record<number, string> = {
    400: 'Check the information and try again.',
    401: 'Your session is no longer valid. Please sign in again.',
    404: 'The requested record was not found.',
    409: 'This record conflicts with existing data.',
    422: 'Some information could not be accepted.',
    500: 'The server could not complete this request. Try again later.',
  };
  return bodyMessage || messages[status] || 'The request could not be completed.';
}

function responseError(error: AxiosError<ApiResponse<unknown>>): ApiError {
  const status = error.response?.status;
  const body = error.response?.data;
  if (!error.response) return new ApiError('Could not reach the service. Check your connection and try again.');
  return new ApiError(httpErrorMessage(status ?? 0, body?.message), status, body?.errorCode);
}

api.interceptors.response.use((response) => response, async (error: AxiosError<ApiResponse<unknown>>) => {
  const config = error.config as RetryConfig | undefined;
  if (error.response?.status === 401 && config && !config._retry && !config.url?.includes('/auth/') && getRefreshToken()) {
    config._retry = true;
    try {
      refreshInFlight ??= axios.post<ApiResponse<{ accessToken: string; refreshToken: string }>>(
        `${apiBaseUrl}/auth/refresh`, { refreshToken: getRefreshToken() },
        { timeout: 15_000, headers: { Accept: 'application/json', 'Content-Type': 'application/json' } },
      ).then((response) => {
        const tokens = response.data.data;
        if (!response.data.success || !tokens?.accessToken || !tokens.refreshToken) throw new Error('Session refresh failed');
        updateTokens(tokens.accessToken, tokens.refreshToken);
        return tokens.accessToken;
      }).finally(() => { refreshInFlight = null; });
      const token = await refreshInFlight;
      config.headers.Authorization = `Bearer ${token}`;
      return api(config);
    } catch {
      clearSession();
      window.dispatchEvent(new CustomEvent('vault:session-expired'));
      return Promise.reject(new ApiError('Your session expired. Please sign in again.', 401, 'SESSION_EXPIRED'));
    }
  }
  return Promise.reject(responseError(error));
});

export async function unwrap<T>(request: Promise<{ data: ApiResponse<T> }>): Promise<T> {
  const response = await request;
  if (!response.data.success) throw new ApiError(response.data.message || 'The service returned an error.');
  return response.data.data as T;
}

export const apiGet = <T>(url: string, config?: AxiosRequestConfig) => unwrap(api.get<ApiResponse<T>>(url, config));
export const apiPost = <T>(url: string, payload?: unknown) => unwrap(api.post<ApiResponse<T>>(url, payload));
export const apiPut = <T>(url: string, payload?: unknown) => unwrap(api.put<ApiResponse<T>>(url, payload));
export const apiDelete = <T = void>(url: string, config?: AxiosRequestConfig) => unwrap(api.delete<ApiResponse<T>>(url, config));

export function toUserMessage(error: unknown, fallback = 'Unable to complete this request. Try again.'): string {
  return error instanceof ApiError ? error.message : error instanceof Error ? error.message : fallback;
}
