import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import type { ReactNode } from 'react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { LoginPage } from './LoginPage';
import { RegisterPage } from './RegisterPage';

const auth = vi.hoisted(() => ({ signIn: vi.fn(), signUp: vi.fn() }));
vi.mock('../../hooks/useAuth', () => ({ useAuth: () => auth }));

function mount(element: ReactNode) { return render(<MemoryRouter>{element}</MemoryRouter>); }

describe('authentication forms', () => {
  beforeEach(() => { vi.clearAllMocks(); });

  it('submits login credentials to the auth provider without exposing them in UI', async () => {
    const user = userEvent.setup();
    auth.signIn.mockResolvedValue(undefined);
    mount(<LoginPage />);
    await user.type(screen.getByLabelText('Email'), 'person@example.com');
    await user.type(screen.getByLabelText('Password'), 'SecretPass!2');
    await user.click(screen.getByRole('button', { name: 'Sign in' }));
    await waitFor(() => expect(auth.signIn).toHaveBeenCalledWith({ email: 'person@example.com', password: 'SecretPass!2' }));
    expect(screen.queryByText('SecretPass!2')).not.toBeInTheDocument();
  });

  it('validates required login fields inline', async () => {
    mount(<LoginPage />);
    fireEvent.submit(screen.getByRole('button', { name: 'Sign in' }).closest('form')!);
    expect(await screen.findByText('Enter a valid email address.')).toBeInTheDocument();
    expect(await screen.findByText('Enter your password.')).toBeInTheDocument();
    expect(auth.signIn).not.toHaveBeenCalled();
  });

  it('submits matching registration values without sending confirmation password', async () => {
    const user = userEvent.setup();
    auth.signUp.mockResolvedValue(undefined);
    mount(<RegisterPage />);
    await user.type(screen.getByLabelText('Full name'), 'Avery Example');
    await user.type(screen.getByLabelText('Email'), 'avery@example.com');
    await user.type(screen.getByLabelText('Password'), 'StrongPass!7');
    await user.type(screen.getByLabelText('Confirm password'), 'StrongPass!7');
    await user.click(screen.getByRole('button', { name: 'Create account' }));
    await waitFor(() => expect(auth.signUp).toHaveBeenCalledWith({ fullName: 'Avery Example', email: 'avery@example.com', password: 'StrongPass!7' }));
  });
});
