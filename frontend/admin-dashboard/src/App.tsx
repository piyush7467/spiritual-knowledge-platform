import React from 'react';
import { ShieldCheck, FileText, Upload, Scale, Video, Cpu, ClipboardList } from 'lucide-react';

const ADMIN_SECTIONS = [
  { icon: FileText, title: 'Sources & Editions', count: 'Registry & Rights' },
  { icon: Upload, title: 'Document Ingestion', count: 'PDF / OCR Queue' },
  { icon: ShieldCheck, title: 'Passage & Translation Review', count: 'Human Verification' },
  { icon: Scale, title: 'Claims, Evidence & Comparisons', count: 'Research Workbench' },
  { icon: Video, title: 'Video & Timestamp Editor', count: 'Verified Segments' },
  { icon: Cpu, title: 'AI Provider & RAG Monitor', count: 'Adapter & Citations' },
  { icon: ClipboardList, title: 'Submissions & Audit Logs', count: 'Moderation & History' },
];

export const App: React.FC = () => {
  return (
    <div className="min-h-screen bg-[var(--bg-canvas)] text-[var(--text-ink)]">
      <header className="border-b border-[var(--border-anchor)] bg-[var(--bg-anchor)] px-6 py-5 text-[var(--text-on-anchor)]">
        <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-4">
          <div>
            <h1 className="font-editorial text-[length:var(--text-h1)] font-semibold">
              Editorial &amp; Super Admin Console
            </h1>
            <p className="text-[length:var(--text-meta)] text-[var(--text-on-anchor-muted)]">
              Unified single-place Spiritual Theme · Verification before publication
            </p>
          </div>
          <button
            type="button"
            className="min-h-[48px] rounded-md border border-[var(--btn-anchor-border)] bg-[var(--btn-anchor-bg)] px-5 py-2.5 text-[length:var(--text-ui)] font-semibold text-[var(--btn-anchor-text)] hover:bg-[var(--btn-anchor-hover)]"
          >
            Review Queue
          </button>
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-6 py-10">
        <div className="divide-y divide-[var(--border-subtle)] rounded-lg border border-[var(--border-strong)] bg-[var(--bg-surface)] shadow-card">
          {ADMIN_SECTIONS.map((sec) => {
            const Icon = sec.icon;
            return (
              <div
                key={sec.title}
                className="flex flex-col gap-3 p-6 sm:flex-row sm:items-center sm:justify-between"
              >
                <div className="flex items-start gap-4">
                  <Icon className="mt-1 h-6 w-6 shrink-0 text-[var(--text-sacred-accent)]" aria-hidden="true" />
                  <div>
                    <h2 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
                      {sec.title}
                    </h2>
                    <p className="text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">
                      {sec.count}
                    </p>
                  </div>
                </div>
                <button
                  type="button"
                  className="min-h-[48px] rounded-md border border-[var(--btn-secondary-border)] bg-[var(--btn-secondary-bg)] px-5 py-2.5 text-[length:var(--text-ui)] font-semibold text-[var(--btn-secondary-text)] hover:bg-[var(--btn-secondary-hover)]"
                >
                  Open Module
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

