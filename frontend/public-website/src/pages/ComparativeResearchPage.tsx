import React from 'react';
import { Language, translations } from '@/i18n/translations';
import { SAMPLE_COMPARISONS } from '@/data/sampleArchive';
import { StatusBadge } from '@/components/ui/StatusBadge';

interface ComparativeResearchPageProps {
  lang: Language;
}

export const ComparativeResearchPage: React.FC<ComparativeResearchPageProps> = ({ lang }) => {
  const t = translations[lang];

  return (
    <div className="mx-auto max-w-6xl px-4 py-10 sm:px-6 sm:py-14">
      <header className="rounded-lg border border-[var(--border-anchor)] bg-[var(--bg-anchor)] p-6 text-[var(--text-on-anchor)] sm:p-8">
        <h1 className="font-editorial text-[length:var(--text-h1)] font-semibold">
          {t.compare.title}
        </h1>
        <p className="mt-2 text-[length:var(--text-body)] text-[var(--text-on-anchor-muted)]">
          {t.compare.subtitle}
        </p>
      </header>

      <div className="mt-8 space-y-8">
        {SAMPLE_COMPARISONS.map((cmp) => (
          <article
            key={cmp.publicId}
            className="rounded-lg border border-[var(--border-strong)] bg-[var(--bg-surface)] p-6 shadow-card sm:p-8"
          >
            <div className="flex flex-wrap items-center justify-between gap-4 border-b border-[var(--border-subtle)] pb-5">
              <h2 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
                {cmp.title[lang]}
              </h2>
              <StatusBadge tone="review" label={`${t.compare.findingLabel}: ${cmp.findingCategory[lang]}`} />
            </div>

            {/* Stacked on mobile, 3 distinct perspective lanes on desktop */}
            <div className="mt-6 grid gap-6 lg:grid-cols-3">
              <section className="rounded-md border border-[var(--border-subtle)] bg-[var(--bg-canvas)] p-5">
                <h3 className="text-[length:var(--text-meta)] font-semibold text-[var(--text-sacred-accent)]">
                  {t.compare.col1}
                </h3>
                <p className="mt-1 text-[length:var(--text-floor)] text-[var(--text-ink-muted)]">
                  {cmp.scripturePerspective.ref}
                </p>
                <p className="mt-3 text-[length:var(--text-body)] text-[var(--text-ink)]">
                  {cmp.scripturePerspective.text[lang]}
                </p>
              </section>

              <section className="rounded-md border border-[var(--border-subtle)] bg-[var(--bg-canvas)] p-5">
                <h3 className="text-[length:var(--text-meta)] font-semibold text-[var(--text-ink-muted)]">
                  {t.compare.col2}
                </h3>
                <p className="mt-1 text-[length:var(--text-floor)] text-[var(--text-ink-muted)]">
                  {cmp.otherCommentators.source}
                </p>
                <p className="mt-3 text-[length:var(--text-body)] text-[var(--text-ink)]">
                  {cmp.otherCommentators.text[lang]}
                </p>
              </section>

              <section className="rounded-md border border-[var(--border-strong)] bg-[var(--bg-surface-muted)] p-5">
                <h3 className="text-[length:var(--text-meta)] font-semibold text-[var(--text-sacred-accent)]">
                  {t.compare.col3}
                </h3>
                <p className="mt-1 text-[length:var(--text-floor)] text-[var(--text-ink-muted)]">
                  {cmp.santRampalJiPerspective.source}
                </p>
                <p className="mt-3 text-[length:var(--text-body)] text-[var(--text-ink)]">
                  {cmp.santRampalJiPerspective.text[lang]}
                </p>
              </section>
            </div>

            <div className="mt-6 rounded-md border border-[var(--border-subtle)] bg-[var(--bg-surface-muted)] p-5">
              <p className="text-[length:var(--text-body)] text-[var(--text-ink)]">
                <strong>{t.compare.findingLabel}: </strong>
                {cmp.editorialSummary[lang]}
              </p>
            </div>
          </article>
        ))}
      </div>
    </div>
  );
};

