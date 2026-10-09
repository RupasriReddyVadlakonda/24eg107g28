import { useCallback } from 'react';
import { ArrowRight, CalendarDays, Mail, Phone, ShieldCheck, UserRound } from 'lucide-react';
import { Link } from 'react-router-dom';
import { PageHeader } from '../../components/ui/PageHeader';
import { ErrorState, LoadingState } from '../../components/ui/StateView';
import { StatusBadge } from '../../components/ui/StatusBadge';
import { useAuth } from '../../hooks/useAuth';
import { useResource } from '../../hooks/useResource';
import { getProfile } from '../../services/authService';
import { formatDate } from '../../utils/format';

function ProfileField({ label, value, icon: Icon, note }: { label: string; value: string; icon: typeof UserRound; note?: string }) {
  return <div className="border-b border-vault-keyline py-4 last:border-0"><dt className="flex items-center gap-2 text-xs font-semibold text-vault-secondary"><Icon size={15} />{label}</dt><dd className="mt-2 text-sm text-vault-lit">{value}</dd>{note && <p className="mt-1 text-xs text-vault-olive">{note}</p>}</div>;
}

export function ProfilePage() {
  const { user } = useAuth();
  const loadProfile = useCallback(() => getProfile(), []);
  const { data: profile, loading, error, reload } = useResource(loadProfile);

  return <>
    <PageHeader title="Profile" description="Account identity and access authorization profile." />
    {loading ? <LoadingState label="Loading account details" /> : error ? <ErrorState error={error} onRetry={reload} /> : null}
    {profile && <>
    <section className="max-w-2xl rounded-xl border border-vault-keyline bg-vault-well px-5 sm:px-7" aria-labelledby="account-heading">
      <div className="flex items-center justify-between gap-4 border-b border-vault-keyline py-5">
        <div>
          <h2 id="account-heading" className="font-display text-xl">Account details</h2>
          <p className="mt-1 text-xs text-vault-secondary">Managed securely by the authentication service.</p>
        </div>
        <StatusBadge>{user?.role ?? 'USER'}</StatusBadge>
      </div>
      <dl>
        <ProfileField label="Full name" value={profile.fullName} icon={UserRound} />
        <ProfileField label="Email" value={profile.email} icon={Mail} />
        {profile.phoneNumber && <ProfileField label="Phone number" value={profile.phoneNumber} icon={Phone} />}
        <ProfileField label="Role" value={profile.role} icon={ShieldCheck} />
        <ProfileField label="Account created" value={formatDate(profile.createdAt)} icon={CalendarDays} />
      </dl>
      <div className="border-t border-vault-keyline py-4">
        <p className="text-xs leading-5 text-vault-secondary">
          Personal identity records such as addresses, phone numbers, and financial details are kept strictly in your encrypted Personal Data Vault.
        </p>
        <Link to="/vault" className="mt-2 inline-flex items-center gap-1.5 text-xs font-semibold text-vault-lit underline decoration-vault-secondary underline-offset-4">
          Manage vault data <ArrowRight size={14} />
        </Link>
      </div>
    </section>
    </>}
  </>;
}
