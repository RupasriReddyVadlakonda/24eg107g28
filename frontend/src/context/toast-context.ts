import { createContext } from 'react';

type ToastKind = 'success' | 'error' | 'info';
export interface ToastApi { showToast: (message: string, kind?: ToastKind) => void }
export const ToastContext = createContext<ToastApi | null>(null);
