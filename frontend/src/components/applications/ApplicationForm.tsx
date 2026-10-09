import { zodResolver } from '@hookform/resolvers/zod';
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import type { AppRegistrationRequest, ThirdPartyApplication } from '../../types/api';
import { Button } from '../ui/Button';
import { TextAreaField, TextField } from '../ui/Field';

const schema = z.object({
  applicationName: z.string().trim().min(2, 'Enter at least 2 characters.').max(100),
  description: z.string().max(2000).optional(),
  redirectUri: z.string().url('Enter a valid absolute URL.').max(2048),
});
type Values = z.infer<typeof schema>;

export function ApplicationForm({ application, onSave, onCancel, busy }: {
  application?: ThirdPartyApplication;
  onSave: (payload: AppRegistrationRequest) => Promise<void>;
  onCancel: () => void;
  busy: boolean;
}) {
  const { register, handleSubmit, reset, formState: { errors } } = useForm<Values>({ resolver: zodResolver(schema), mode: 'onBlur', defaultValues: {
    applicationName: application?.applicationName ?? '', description: application?.description ?? '', redirectUri: application?.redirectUri ?? '',
  } });
  useEffect(() => { reset({ applicationName: application?.applicationName ?? '', description: application?.description ?? '', redirectUri: application?.redirectUri ?? '' }); }, [application, reset]);
  return <form onSubmit={handleSubmit(async (values) => onSave(values))} className="space-y-5" noValidate>
    <TextField id="applicationName" label="Application name" required error={errors.applicationName?.message} {...register('applicationName')} />
    <TextAreaField id="applicationDescription" label="Description" maxLength={2000} hint="Describe why this application needs access." error={errors.description?.message} {...register('description')} />
    <TextField id="redirectUri" label="Redirect URI" type="url" required placeholder="https://example.com/authorize/callback" error={errors.redirectUri?.message} {...register('redirectUri')} />
    {application && <p className="text-xs text-vault-secondary">Client credentials cannot be viewed or changed here.</p>}
    <div className="flex flex-col-reverse justify-end gap-2 sm:flex-row"><Button type="button" variant="outline" onClick={onCancel}>Cancel</Button><Button type="submit" disabled={busy}>{busy ? 'Saving…' : application ? 'Save changes' : 'Register application'}</Button></div>
  </form>;
}
