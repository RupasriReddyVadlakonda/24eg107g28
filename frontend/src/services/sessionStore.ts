import type { AuthResponse } from '../types/api';
import type { SessionUser, StoredSession } from '../types/session';

const REFRESH_KEY = 'personal-data-vault.refresh.v1';
let activeSession: StoredSession | null = null;

export function readSession(): StoredSession | null { return activeSession; }
export function hasRefreshToken(): boolean { return Boolean(activeSession?.refreshToken || sessionStorage.getItem(REFRESH_KEY)); }

export function writeSession(auth: AuthResponse, fullName?: string): StoredSession {
  const existingName = activeSession?.user.email === auth.email ? activeSession.user.fullName : undefined;
  const user: SessionUser = { userId: auth.userId, email: auth.email, role: auth.role, fullName: fullName ?? existingName };
  sessionStorage.setItem(REFRESH_KEY, auth.refreshToken);
  activeSession = { accessToken: auth.accessToken, refreshToken: auth.refreshToken, user };
  return activeSession;
}

export function updateTokens(accessToken: string, refreshToken: string): StoredSession | null {
  const refresh = refreshToken || activeSession?.refreshToken || sessionStorage.getItem(REFRESH_KEY);
  if (!refresh) return null;
  sessionStorage.setItem(REFRESH_KEY, refresh);
  if (!activeSession) return null;
  activeSession = { ...activeSession, accessToken, refreshToken: refresh };
  return activeSession;
}

export function clearSession(): void {
  activeSession = null;
  sessionStorage.removeItem(REFRESH_KEY);
}
export function getSessionUser(): SessionUser | null { return activeSession?.user ?? null; }
export function getAccessToken(): string | null { return activeSession?.accessToken ?? null; }
export function getRefreshToken(): string | null { return activeSession?.refreshToken ?? sessionStorage.getItem(REFRESH_KEY); }
