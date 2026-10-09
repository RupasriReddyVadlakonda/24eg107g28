import { useCallback, useEffect, useMemo, useState, type ReactNode } from 'react';
import type { AuthResponse, LoginRequest, RegisterRequest } from '../types/api';
import type { StoredSession } from '../types/session';
import * as authService from '../services/authService';
import { clearSession, getRefreshToken, readSession, writeSession } from '../services/sessionStore';
import { AuthContext } from './auth-context';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [session, setSession] = useState<StoredSession | null>(() => readSession());
  const [ready, setReady] = useState(() => !getRefreshToken());

  useEffect(() => {
    let mounted = true;
    const refreshToken = getRefreshToken();
    if (refreshToken && !readSession()) {
      authService.refreshSession(refreshToken)
        .then((response) => { if (mounted) setSession(writeSession(response)); })
        .catch(() => { if (mounted) { clearSession(); setSession(null); } })
        .finally(() => { if (mounted) setReady(true); });
    }
    const expireSession = () => { clearSession(); setSession(null); setReady(true); };
    window.addEventListener('vault:session-expired', expireSession);
    return () => { mounted = false; window.removeEventListener('vault:session-expired', expireSession); };
  }, []);

  const acceptAuth = useCallback((response: AuthResponse, fullName?: string) => { setSession(writeSession(response, fullName)); }, []);
  const signIn = useCallback(async (request: LoginRequest) => acceptAuth(await authService.login(request)), [acceptAuth]);
  const signUp = useCallback(async (request: RegisterRequest) => acceptAuth(await authService.register(request), request.fullName), [acceptAuth]);
  const signOut = useCallback(async () => {
    const current = readSession();
    setReady(false);
    try { if (current) await authService.logout(current.refreshToken); }
    finally { clearSession(); setSession(null); setReady(true); }
  }, []);
  const value = useMemo(() => ({ user: session?.user ?? null, ready, signIn, signUp, signOut }), [session, ready, signIn, signUp, signOut]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
