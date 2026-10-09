export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  errorCode?: string;
}

export type UserRole = 'USER' | 'ADMIN';
export type DataType =
  | 'NAME'
  | 'EMAIL'
  | 'PHONE'
  | 'ADDRESS'
  | 'DATE_OF_BIRTH'
  | 'NATIONAL_ID'
  | 'PASSPORT'
  | 'FINANCIAL_INFORMATION'
  | 'HEALTH_INFORMATION'
  | 'CUSTOM';
export type AllowedOperation = 'READ' | 'WRITE' | 'READ_WRITE';
export type ConsentStatus = 'PENDING' | 'GRANTED' | 'DENIED' | 'REVOKED' | 'EXPIRED';
export type AlertSeverity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  userId: number;
  email: string;
  role: UserRole;
}

export interface AuthUser {
  userId: number;
  email: string;
  role: UserRole;
  fullName?: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  phoneNumber?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface PersonalData {
  id: number;
  dataType: DataType;
  value: string;
  description: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface PersonalDataRequest {
  dataType: DataType;
  value: string;
  description?: string;
}

export interface Consent {
  id: number;
  userId: number;
  applicationId: number;
  applicationName: string;
  dataType: DataType;
  purpose: string;
  allowedOperation: AllowedOperation;
  status: ConsentStatus;
  startTime: string | null;
  expirationTime: string | null;
  requestedDurationDays: number;
  createdAt: string;
  revokedAt: string | null;
}

export interface ConsentRequest {
  applicationId: number;
  userId?: number;
  dataType: DataType;
  purpose: string;
  operation: AllowedOperation;
  requestedDurationDays: number;
}

export interface ThirdPartyApplication {
  id: number;
  applicationName: string;
  clientId: string;
  description: string | null;
  redirectUri: string;
  active: boolean;
  createdAt: string;
}

export interface AppRegistrationRequest {
  applicationName: string;
  description?: string;
  redirectUri: string;
}

export type AppUpdateRequest = AppRegistrationRequest;

export interface AppRegistrationResponse {
  id: number;
  applicationName: string;
  clientId: string;
  clientSecret: string;
  redirectUri: string;
  message: string;
}

export interface AccessLog {
  id: number;
  userId: number;
  applicationId: number | null;
  applicationName: string | null;
  dataType: DataType | null;
  operation: string | null;
  purpose: string | null;
  timestamp: string;
  ipAddress: string | null;
  success: boolean;
  failureReason: string | null;
}

export interface SecurityAlert {
  id: number;
  userId: number | null;
  applicationId: number | null;
  alertType: string;
  severity: AlertSeverity;
  description: string;
  timestamp: string;
  resolved: boolean;
}

export interface UserRecord {
  id: number;
  fullName: string;
  email: string;
  phoneNumber: string | null;
  role: UserRole;
  enabled: boolean;
  createdAt: string;
}

export type ApiData<T> = T | { content: T[]; totalElements?: number; number?: number; size?: number };
