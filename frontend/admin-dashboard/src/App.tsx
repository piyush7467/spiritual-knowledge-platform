import React, { useState } from 'react';
import {
  ShieldCheck,
  FileText,
  Upload,
  Scale,
  Video,
  Cpu,
  ClipboardList,
  LogOut,
  Lock,
} from 'lucide-react';

interface AdminSession {
  accessToken: string;
  email: string;
  displayName: string;
  roles: string[];
}

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

const ADMIN_SECTIONS = [
  { key: 'sources', icon: FileText, title: 'Sources & Editions', subtitle: 'Registry, editions & rights metadata' },
  { key: 'ingestion', icon: Upload, title: 'Document Ingestion', subtitle: 'Signed uploads & PDF / OCR job queue' },
  { key: 'review', icon: ShieldCheck, title: 'Passage & Translation Review', subtitle: 'Human verification before publishing' },
  { key: 'research', icon: Scale, title: 'Claims, Evidence & Comparisons', subtitle: 'Three-perspective research workbench' },
  { key: 'videos', icon: Video, title: 'Video & Timestamp Editor', subtitle: 'Permitted links & integer-second segments' },
  { key: 'ai', icon: Cpu, title: 'AI Provider & RAG Monitor', subtitle: 'Provider adapters & citation checks' },
  { key: 'audit', icon: ClipboardList, title: 'Submissions & Audit Logs', subtitle: 'Moderation & immutable history' },
];

export const App: React.FC = () => {
  const [session, setSession] = useState<AdminSession | null>(() => {
    const raw = sessionStorage.getItem('skp_admin_session');
    return raw ? (JSON.parse(raw) as AdminSession) : null;
  });
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<string>('review');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const res = await fetch(`${API_BASE}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email.trim(), password }),
      });
      const payload = await res.json();
      if (!res.ok || !payload.success) {
        throw new Error(payload?.error?.message || 'Sign-in failed. Verify email, password, and backend status.');
      }
      const roles: string[] = payload.data.roles || [];
      const hasAdminAccess = roles.some((r) => ['SUPER_ADMIN', 'ADMIN', 'REVIEWER'].includes(r));
      if (!hasAdminAccess) {
        throw new Error('This account does not have Editorial or Admin permissions.');
      }
      const nextSession: AdminSession = {
        accessToken: payload.data.accessToken,
        email: payload.data.email,
        displayName: payload.data.displayName,
        roles,
      };
      sessionStorage.setItem('skp_admin_session', JSON.stringify(nextSession));
      setSession(nextSession);
      setPassword('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unable to reach authentication server.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    sessionStorage.removeItem('skp_admin_session');
    setSession(null);
  };

  if (!session) {
    return (
      <div className="min-h-screen bg-[var(--bg-canvas)] text-[var(--text-ink)] flex flex-col justify-center px-4 py-12">
        <div className="mx-auto w-full max-w-lg rounded-lg border border-[var(--border-anchor)] bg-[var(--bg-anchor)] p-8 text-[var(--text-on-anchor)] shadow-anchor">
          <div className="flex items-center gap-3">
            <Lock className="h-7 w-7 text-[var(--btn-anchor-bg)]" aria-hidden="true" />
            <h1 className="font-editorial text-[length:var(--text-h1)] font-semibold">
              Editorial &amp; Admin Sign In
            </h1>
          </div>
          <p className="mt-2 text-[length:var(--text-body)] text-[var(--text-on-anchor-muted)]">
            Sign in with your Super Admin or Reviewer account to manage sources, verify passages, and publish evidence.
          </p>

          <form onSubmit={handleLogin} className="mt-7 space-y-5">
            <div>
              <label htmlFor="admin-email" className="block text-[length:var(--text-meta)] font-medium text-[var(--text-on-anchor)]">
                Admin Email
              </label>
              <input
                id="admin-email"
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="admin@yourdomain.com"
                className="mt-2 min-h-[50px] w-full rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] px-4 text-[length:var(--text-ui)] text-[var(--text-on-anchor)] placeholder:text-[var(--text-on-anchor-muted)]"
              />
            </div>

            <div>
              <label htmlFor="admin-password" className="block text-[length:var(--text-meta)] font-medium text-[var(--text-on-anchor)]">
                Password
              </label>
              <input
                id="admin-password"
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter password"
                className="mt-2 min-h-[50px] w-full rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] px-4 text-[length:var(--text-ui)] text-[var(--text-on-anchor)] placeholder:text-[var(--text-on-anchor-muted)]"
              />
            </div>

            {error && (
              <div className="rounded-md border border-[var(--badge-alert-border)] bg-[var(--badge-alert-bg)] px-4 py-3 text-[length:var(--text-meta)] text-[var(--badge-alert-text)]">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="min-h-[50px] w-full rounded-md border border-[var(--btn-primary-border)] bg-[var(--btn-primary-bg)] px-6 py-3 text-[length:var(--text-ui)] font-semibold text-[var(--btn-primary-text)] hover:bg-[var(--btn-primary-hover)] disabled:opacity-60"
            >
              {loading ? 'Signing In...' : 'Sign In to Admin Console'}
            </button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[var(--bg-canvas)] text-[var(--text-ink)]">
      <header className="border-b border-[var(--border-anchor)] bg-[var(--bg-anchor)] px-6 py-5 text-[var(--text-on-anchor)]">
        <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-4">
          <div>
            <h1 className="font-editorial text-[length:var(--text-h1)] font-semibold">
              Editorial &amp; Super Admin Console
            </h1>
            <p className="text-[length:var(--text-meta)] text-[var(--text-on-anchor-muted)]">
              Signed in as {session.displayName} ({session.roles.join(', ')})
            </p>
          </div>
          <div className="flex flex-wrap gap-3">
            <button
              type="button"
              onClick={() => setActiveTab('review')}
              className="min-h-[48px] rounded-md border border-[var(--btn-anchor-border)] bg-[var(--btn-anchor-bg)] px-5 py-2.5 text-[length:var(--text-ui)] font-semibold text-[var(--btn-anchor-text)] hover:bg-[var(--btn-anchor-hover)]"
            >
              Review Queue
            </button>
            <button
              type="button"
              onClick={handleLogout}
              className="inline-flex min-h-[48px] items-center gap-2 rounded-md border border-[var(--btn-secondary-border)] bg-[var(--btn-secondary-bg)] px-5 py-2.5 text-[length:var(--text-ui)] font-semibold text-[var(--btn-secondary-text)] hover:bg-[var(--btn-secondary-hover)]"
            >
              <LogOut className="h-5 w-5" aria-hidden="true" />
              <span>Sign Out</span>
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-6 py-10">
        <div className="divide-y divide-[var(--border-subtle)] rounded-lg border border-[var(--border-strong)] bg-[var(--bg-surface)] shadow-card">
          {ADMIN_SECTIONS.map((sec) => {
            const Icon = sec.icon;
            const isSelected = activeTab === sec.key;
            return (
              <div
                key={sec.key}
                className={`flex flex-col gap-3 p-6 sm:flex-row sm:items-center sm:justify-between ${
                  isSelected ? 'bg-[var(--bg-surface-muted)]' : ''
                }`}
              >
                <div className="flex items-start gap-4">
                  <Icon className="mt-1 h-6 w-6 shrink-0 text-[var(--text-sacred-accent)]" aria-hidden="true" />
                  <div>
                    <h2 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
                      {sec.title}
                    </h2>
                    <p className="text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">
                      {sec.subtitle}
                    </p>
                  </div>
                </div>
                <button
                  type="button"
                  onClick={() => setActiveTab(sec.key)}
                  className={`min-h-[48px] rounded-md px-5 py-2.5 text-[length:var(--text-ui)] font-semibold ${
                    isSelected
                      ? 'border border-[var(--btn-primary-border)] bg-[var(--btn-primary-bg)] text-[var(--btn-primary-text)] hover:bg-[var(--btn-primary-hover)]'
                      : 'border border-[var(--btn-secondary-border)] bg-[var(--btn-secondary-bg)] text-[var(--btn-secondary-text)] hover:bg-[var(--btn-secondary-hover)]'
                  }`}
                >
                  {isSelected ? 'Active Module' : 'Open Module'}
                </button>
              </div>
            );
          })}
        </div>
      </main>
    </div>
  );
};

export default App;
