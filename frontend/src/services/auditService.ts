import type { AccessLog } from '../types/api';
import { apiGet } from './api';

export interface AuditPage { records: AccessLog[]; hasMore: boolean; page: number }
export const auditService = {
  ownPage: async (page = 0, size = 100): Promise<AuditPage> => {
    const records = await apiGet<AccessLog[]>('/audit/logs', { params: { page, size } });
    return { records, hasMore: records.length === size, page };
  },
  adminPage: async (page = 0, size = 100): Promise<AuditPage> => {
    const records = await apiGet<AccessLog[]>('/admin/audit', { params: { page, size } });
    return { records, hasMore: records.length === size, page };
  },
};
