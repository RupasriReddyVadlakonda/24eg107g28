import type { AllowedOperation, DataType } from '../types/api';

export const DATA_TYPES = [
  'NAME', 'EMAIL', 'PHONE', 'ADDRESS', 'DATE_OF_BIRTH', 'NATIONAL_ID', 'PASSPORT',
  'FINANCIAL_INFORMATION', 'HEALTH_INFORMATION', 'CUSTOM',
] as const satisfies readonly DataType[];
export const OPERATIONS: AllowedOperation[] = ['READ', 'WRITE', 'READ_WRITE'];
