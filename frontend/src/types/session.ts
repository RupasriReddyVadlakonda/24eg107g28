import type { UserRole } from './api';

export interface SessionUser {
  userId: number;
  email: string;
  role: UserRole;
  fullName?: string;
}

export interface StoredSession {
  user: SessionUser;
  accessToken: string;
  refreshToken: string;
}
