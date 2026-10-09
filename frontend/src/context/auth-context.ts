import { createContext } from 'react';
import type { LoginRequest, RegisterRequest } from '../types/api';
import type { SessionUser } from '../types/session';

export interface AuthContextValue {
  user: SessionUser | null;
  ready: boolean;
  signIn: (request: LoginRequest) => Promise<void>;
  signUp: (request: RegisterRequest) => Promise<void>;
  signOut: () => Promise<void>;
}

export const AuthContext = createContext<AuthContextValue | null>(null);
