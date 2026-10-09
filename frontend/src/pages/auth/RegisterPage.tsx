import { zodResolver } from '@hookform/resolvers/zod';
import { ArrowRight, UserRound } from 'lucide-react';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import { z } from 'zod';
import { Button } from '../../components/ui/Button';
import { TextField } from '../../components/ui/Field';
import { useAuth } from '../../hooks/useAuth';
import { toUserMessage } from '../../services/api';

const schema = z.object({
  fullName: z.string().trim().min(2, 'Enter at least 2 characters.').max(100),
  email: z.string().trim().email('Enter a valid email address.'),
  password: z.string().min(8, 'Use at least 8 characters.').max(100)
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/, 'Include uppercase, lowercase, a number, and one of @$!%*?&.'),
  confirmPassword: z.string().min(1, 'Confirm your password.'),
}).refine((values) => values.password === values.confirmPassword, {
  path: ['confirmPassword'], message: 'Passwords do not match.',
});
type RegisterValues = z.infer<typeof schema>;

export function RegisterPage() {
  const { signUp } = useAuth();
  const navigate = useNavigate();
  const [requestError, setRequestError] = useState('');
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<RegisterValues>({ resolver: zodResolver(schema), mode: 'onBlur' });
  const submit = handleSubmit(async (values) => {
    setRequestError('');
    try {
      await signUp({ fullName: values.fullName, email: values.email, password: values.password });
      navigate('/dashboard', { replace: true });
    } catch (error) {
      setRequestError(toUserMessage(error, 'Account creation could not be completed. Try again.'));
    }
  });

  return <div>
    <h1 className="font-display text-3xl text-vault-lit sm:text-4xl">Create your account</h1>
    <p className="mt-2 text-sm text-vault-secondary">Your account keeps your records and permissions together.</p>
    <form onSubmit={submit} className="mt-8 space-y-5" noValidate>
      {requestError && <div role="alert" className="rounded-lg border border-vault-alert/70 bg-vault-alert/15 px-4 py-3 text-sm text-[#F0A497]">{requestError}</div>}
      <TextField id="fullName" label="Full name" autoComplete="name" required leading={<UserRound size={17} />} error={errors.fullName?.message} {...register('fullName')} />
      <TextField id="email" label="Email" type="email" autoComplete="email" required placeholder="you@example.com" error={errors.email?.message} {...register('email')} />
      <TextField id="password" label="Password" type="password" autoComplete="new-password" required hint="At least 8 characters with uppercase, lowercase, number, and special character." error={errors.password?.message} {...register('password')} />
      <TextField id="confirmPassword" label="Confirm password" type="password" autoComplete="new-password" required error={errors.confirmPassword?.message} {...register('confirmPassword')} />
      <Button type="submit" disabled={isSubmitting} className="w-full" icon={<ArrowRight size={16} />}>{isSubmitting ? 'Creating account…' : 'Create account'}</Button>
    </form>
    <p className="mt-6 text-sm text-vault-secondary">Already have an account? <Link to="/login" className="font-semibold text-vault-lit underline decoration-vault-secondary underline-offset-4">Sign in</Link></p>
  </div>;
}
