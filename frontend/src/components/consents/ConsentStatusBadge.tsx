import type { ConsentStatus } from '../../types/api';
import { StatusBadge } from '../ui/StatusBadge';

export function ConsentStatusBadge({ status }: { status: ConsentStatus }) {
  return <StatusBadge>{status}</StatusBadge>;
}
