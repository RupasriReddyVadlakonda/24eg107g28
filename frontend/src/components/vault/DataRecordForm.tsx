import { zodResolver } from '@hookform/resolvers/zod';
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { Button } from '../ui/Button';
import { SelectField, TextAreaField, TextField } from '../ui/Field';
import { DATA_TYPES } from '../../utils/options';
import type { DataType, PersonalData, PersonalDataRequest } from '../../types/api';

const schema = z.object({
  dataType: z.enum(DATA_TYPES),
  value: z.string().trim().min(1, 'Enter a value.').max(10000),
  description: z.string().max(500).optional(),
});
type Values = z.infer<typeof schema>;

export function DataRecordForm({ record, onSave, onCancel, busy }: {
  record?: PersonalData;
  onSave: (payload: PersonalDataRequest) => Promise<void>;
  onCancel: () => void;
  busy: boolean;
}) {
  const { register, handleSubmit, reset, formState: { errors } } = useForm<Values>({
    resolver: zodResolver(schema), mode: 'onBlur',
    defaultValues: { dataType: record?.dataType ?? 'EMAIL', value: record?.value ?? '', description: record?.description ?? '' },
  });
  useEffect(() => {
    reset({ dataType: record?.dataType ?? 'EMAIL', value: record?.value ?? '', description: record?.description ?? '' });
  }, [record, reset]);
  const submit = handleSubmit(async (values) => onSave({ ...values, dataType: values.dataType as DataType }));

  return <form onSubmit={submit} className="space-y-5" noValidate>
    <p className="text-sm leading-6 text-vault-secondary">Values are encrypted before storage. This form sends the value only when you save this record.</p>
    <SelectField id="dataType" label="Data type" required error={errors.dataType?.message} {...register('dataType')}>
      {DATA_TYPES.map((type) => <option key={type} value={type}>{type.replaceAll('_', ' ').toLowerCase().replace(/\b\w/g, (letter) => letter.toUpperCase())}</option>)}
    </SelectField>
    <TextField id="recordValue" label="Value" required autoComplete="off" error={errors.value?.message} {...register('value')} />
    <TextAreaField id="description" label="Description (optional)" maxLength={500} hint="Do not include more information than needed." error={errors.description?.message} {...register('description')} />
    <div className="flex flex-col-reverse justify-end gap-2 pt-2 sm:flex-row">
      <Button type="button" variant="outline" onClick={onCancel}>Cancel</Button>
      <Button type="submit" disabled={busy}>{busy ? 'Saving…' : record ? 'Save changes' : 'Add to vault'}</Button>
    </div>
  </form>;
}
