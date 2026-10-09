import { zodResolver } from '@hookform/resolvers/zod';
import { ArrowRight, LockKeyhole, Mail } from 'lucide-react';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { Button } from '../../components/ui/Button';
import { TextField } from '../../components/ui/Field';
import { useAuth } from '../../hooks/useAuth';
import { toUserMessage } from '../../services/api';

const schema = z.object({
  email: z.string().email('Enter a valid email address.'),
  password: z.string().min(1, 'Enter your password.').max(100),
});
type LoginValues = z.infer<typeof schema>;

export function LoginPage() {
  const { signIn } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [requestError, setRequestError] = useState('');
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<LoginValues>({ resolver: zodResolver(schema), mode: 'onBlur' });

  const submit = handleSubmit(async (values) => {
    setRequestError('');
    try {
      await signIn(values);
      const destination = (location.state as { from?: string } | null)?.from ?? '/dashboard';
      navigate(destination, { replace: true });
    } catch (error) {
      setRequestError(toUserMessage(error, 'Sign in could not be completed. Check your details and try again.'));
    }
  });

  return <div>
    <h1 className="font-display text-3xl text-vault-lit sm:text-4xl">Sign in</h1>
    <p className="mt-2 text-sm text-vault-secondary">Access your personal data vault.</p>
    <form onSubmit={submit} className="mt-8 space-y-5" noValidate>
      {requestError && <div role="alert" className="rounded-lg border border-vault-alert/70 bg-vault-alert/15 px-4 py-3 text-sm text-[#F0A497]">{requestError}</div>}
      <TextField id="email" label="Email" type="email" autoComplete="email" required placeholder="you@example.com" leading={<Mail size={17} />} error={errors.email?.message} {...register('email')} />
      <TextField id="password" label="Password" type="password" autoComplete="current-password" required leading={<LockKeyhole size={17} />} error={errors.password?.message} {...register('password')} />
      <Button type="submit" disabled={isSubmitting} className="w-full" icon={<ArrowRight size={16} />}>{isSubmitting ? 'Signing in…' : 'Sign in'}</Button>
    </form>
    <p className="mt-6 text-sm text-vault-secondary">New to the vault? <Link to="/register" className="font-semibold text-vault-lit underline decoration-vault-secondary underline-offset-4">Create account</Link></p>
  </div>;
}
