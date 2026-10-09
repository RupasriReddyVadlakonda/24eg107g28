import { Link, Outlet } from 'react-router-dom';
import { Database, ShieldCheck } from 'lucide-react';

export function AuthLayout() {
  return <main className="grid min-h-screen bg-vault-page lg:grid-cols-[minmax(20rem,0.88fr)_minmax(28rem,1.12fr)]">
    <section className="relative hidden flex-col justify-between overflow-hidden bg-vault-nav p-10 lg:flex xl:p-14">
      <div>
        <Link to="/login" className="inline-flex items-center gap-3" aria-label="Personal Data Vault home">
          <span className="flex size-11 items-center justify-center rounded-lg border border-vault-keyline text-vault-secondary"><Database size={21} /></span>
          <span className="font-display text-lg leading-tight text-vault-lit">Personal Data<br />Vault</span>
        </Link>
      </div>
      <div className="max-w-lg">
        <p className="font-display text-4xl leading-[1.15] text-vault-lit xl:text-5xl">Your information.<br /><span className="text-vault-secondary">Your decision.</span></p>
        <p className="mt-5 max-w-md text-base leading-7 text-vault-secondary">Keep personal records in one encrypted vault and review every request before an application can access them.</p>
      </div>
      <p className="flex items-center gap-2 text-sm text-vault-secondary"><ShieldCheck size={17} />AES-256-GCM encrypted storage</p>
      <div aria-hidden="true" className="pointer-events-none absolute -bottom-20 -right-14 size-80 rounded-full border border-vault-keyline/70" />
    </section>
    <section className="flex min-h-screen flex-col justify-center px-5 py-10 sm:px-10 lg:px-14 xl:px-24">
      <div className="mx-auto mb-8 flex items-center gap-3 lg:hidden">
        <span className="flex size-10 items-center justify-center rounded-lg border border-vault-keyline text-vault-secondary"><Database size={19} /></span>
        <span className="font-display text-lg text-vault-lit">Personal Data Vault</span>
      </div>
      <div className="mx-auto w-full max-w-md"><Outlet /></div>
    </section>
  </main>;
}
