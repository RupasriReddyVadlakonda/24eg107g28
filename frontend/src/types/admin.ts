import type { AccessLog, Consent, SecurityAlert, ThirdPartyApplication, UserRecord } from './api';

export interface AdminData {
  users: UserRecord[] | null;
  apps: ThirdPartyApplication[] | null;
  consents: Consent[] | null;
  alerts: SecurityAlert[] | null;
  audit: AccessLog[] | null;
  errors: string[];
}
