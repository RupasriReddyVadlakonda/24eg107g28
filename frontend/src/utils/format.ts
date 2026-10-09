import type { DataType } from '../types/api';

const DATA_TYPE_LABELS: Record<DataType, string> = {
  NAME: 'Name',
  EMAIL: 'Email',
  PHONE: 'Phone',
  ADDRESS: 'Address',
  DATE_OF_BIRTH: 'Date of birth',
  NATIONAL_ID: 'National ID',
  PASSPORT: 'Passport',
  FINANCIAL_INFORMATION: 'Financial information',
  HEALTH_INFORMATION: 'Health information',
  CUSTOM: 'Custom',
};

export function labelForDataType(type: DataType | null | undefined): string {
  return type ? DATA_TYPE_LABELS[type] ?? type.replaceAll('_', ' ').toLowerCase() : 'Unavailable';
}

export function formatDateTime(value: string | null | undefined): string {
  if (!value) return 'Not provided';
  const date = new Date(value);
  return Number.isNaN(date.getTime())
    ? 'Not provided'
    : new Intl.DateTimeFormat(undefined, { dateStyle: 'medium', timeStyle: 'short' }).format(date);
}

export function formatDate(value: string | null | undefined): string {
  if (!value) return 'Not provided';
  const date = new Date(value);
  return Number.isNaN(date.getTime())
    ? 'Not provided'
    : new Intl.DateTimeFormat(undefined, { dateStyle: 'medium' }).format(date);
}

export function maskSensitiveValue(type: DataType, value: string): string {
  if (!value) return '••••••••';
  if (type === 'EMAIL') {
    const [local, domain] = value.split('@');
    if (local && domain) return `${local[0]}${'•'.repeat(Math.min(Math.max(local.length - 1, 3), 8))}@${domain}`;
  }
  if (type === 'PHONE') {
    if (value.length <= 4) return '•'.repeat(Math.max(value.length, 6));
    return `${'•'.repeat(value.length - 4)}${value.slice(-4)}`;
  }
  if (value.length <= 4) return '•'.repeat(Math.max(value.length, 6));
  return `${'•'.repeat(Math.min(Math.max(value.length - 2, 4), 12))}${value.slice(-2)}`;
}

export function getErrorMessage(error: unknown, fallback = 'Unable to complete this request. Try again.'): string {
  if (error instanceof Error && error.message) return error.message;
  return fallback;
}
