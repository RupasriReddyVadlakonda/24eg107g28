import { useContext } from 'react';
import { ToastContext } from '../context/toast-context';
import type { ToastApi } from '../context/toast-context';

export function useToast(): ToastApi {
  const context = useContext(ToastContext);
  if (!context) throw new Error('useToast must be used inside ToastProvider');
  return context;
}
