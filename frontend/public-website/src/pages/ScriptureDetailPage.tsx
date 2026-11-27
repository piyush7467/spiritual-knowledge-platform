import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { ArrowLeft, BookMarked, FileCheck2 } from 'lucide-react';
import { Language, translations } from '@/i18n/translations';
import { SAMPLE_PASSAGES } from '@/data/sampleArchive';
import { StatusBadge } from '@/components/ui/StatusBadge';
import { SpiritualButton } from '@/components/ui/SpiritualButton';

interface ScriptureDetailPageProps {
  lang: Language;
}

export const ScriptureDetailPage: React.FC<ScriptureDetailPageProps> = ({ lang }) => {
  const { publicId } = useParams<{ publicId: string }>();
  const t = translations[lang];
  const passage = SAMPLE_PASSAGES.find((p) => p.publicId === publicId) ?? SAMPLE_PASSAGES[0];

  return (
    <div className="mx-auto max-w-5xl px-4 py-10 sm:px-6 sm:py-14">
      <Link to="/search" className="inline-block">
        <SpiritualButton variant="outline">
          <ArrowLeft className="h-5 w-5" aria-hidden="true" />
          <span>{t.reader.backToSearch}</span>
        </SpiritualButton>
      </Link>

      {/* Dark Anchor Manuscript Header */}
      <header className="mt-6 rounded-lg border border-[var(--border-anchor)] bg-[var(--bg-anchor)] p-6 text-[var(--text-on-anchor)] sm:p-8">
        <div className="flex flex-wrap items-center justify-between gap-4">
          <div>
            <p className="text-[length:var(--text-meta)] text-[var(--text-on-anchor-muted)]">
              {passage.referenceHierarchy[lang]}
            </p>
            <h1 className="mt-1 font-editorial text-[length:var(--text-h1)] font-semibold">
              {passage.sourceTitle[lang]}
            </h1>
          </div>
          <StatusBadge tone="verified" label={t.featured.verifiedStatus} />
        </div>

        <div className="mt-5 flex flex-wrap gap-6 border-t border-[var(--border-anchor)] pt-4 text-[length:var(--text-meta)] text-[var(--text-on-anchor-muted)]">
          <span>
            {t.featured.printedPage}: <strong className="text-[var(--text-on-anchor)]">{passage.printedPage}</strong>
          </span>
          <span>
            {t.featured.pdfPage}: <strong className="text-[var(--text-on-anchor)]">{passage.pdfPage}</strong>
          </span>
          <span>
            Ref ID: <strong className="text-[var(--text-on-anchor)]">{passage.canonicalRef}</strong>
          </span>
        </div>
      </header>

      {/* Layer 1: Original Text */}
      <section className="mt-8 rounded-lg border border-[var(--border-strong)] bg-[var(--bg-surface)] p-6 shadow-card sm:p-8">
        <h2 className="flex items-center gap-2.5 text-[length:var(--text-meta)] font-semibold text-[var(--text-sacred-accent)]">
          <BookMarked className="h-5 w-5" aria-hidden="true" />
          <span>
            {t.featured.originalTextLabel} — {passage.originalLanguageLabel[lang]}
          </span>
        </h2>
        <p
          lang="hi"
          className="scripture-prose mt-4 rounded-md border border-[var(--border-subtle)] bg-[var(--bg-canvas)] p-6 font-medium text-[var(--text-ink)]"
        >
          {passage.originalScript}
        </p>

        {/* Layer 2: Published Translation */}
        <h2 className="mt-8 text-[length:var(--text-meta)] font-semibold text-[var(--text-ink-muted)]">
          {t.featured.translationLabel}
        </h2>
        <p className="mt-2 max-w-[var(--measure-reading)] text-[length:var(--text-scripture)] leading-relaxed text-[var(--text-ink)]">
          {passage.publishedTranslation[lang]}
        </p>

        {/* Layer 4: Attribution / Usage Context */}
        <div className="mt-8 rounded-md border border-[var(--border-subtle)] bg-[var(--bg-surface-muted)] p-5">
          <h3 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
            {t.featured.interpretationLabel}
          </h3>
          <p className="mt-2 text-[length:var(--text-body)] text-[var(--text-ink)]">
            {passage.attributionContext[lang]}
          </p>
        </div>
      </section>

      {/* Surrounding Context */}
      <section className="mt-6 rounded-lg border border-[var(--border-subtle)] bg-[var(--bg-surface)] p-6">
        <h2 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
          {t.reader.surroundingContext}
        </h2>
        <div className="mt-4 grid gap-4 sm:grid-cols-2">
          <div className="rounded-md border border-[var(--border-subtle)] bg-[var(--bg-canvas)] p-4">
            <p className="text-[length:var(--text-body)] text-[var(--text-ink)]">{passage.contextBefore[lang]}</p>
          </div>
          <div className="rounded-md border border-[var(--border-subtle)] bg-[var(--bg-canvas)] p-4">
            <p className="text-[length:var(--text-body)] text-[var(--text-ink)]">{passage.contextAfter[lang]}</p>
          </div>
        </div>
      </section>

      {/* Source Provenance & Rights */}
      <section className="mt-6 rounded-lg border border-[var(--border-subtle)] bg-[var(--bg-surface-muted)] p-6">
        <h2 className="flex items-center gap-2 font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
          <FileCheck2 className="h-6 w-6 text-[var(--text-sacred-accent)]" aria-hidden="true" />
          <span>{t.reader.sourceMetadata}</span>
        </h2>
        <dl className="mt-4 grid gap-4 text-[length:var(--text-body)] sm:grid-cols-2">
          <div>
            <dt className="text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">{t.reader.edition}</dt>
            <dd className="font-medium text-[var(--text-ink)]">{passage.edition[lang]}</dd>
          </div>
          <div>
            <dt className="text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">{t.reader.publisher}</dt>
            <dd className="font-medium text-[var(--text-ink)]">{passage.publisher[lang]}</dd>
          </div>
          <div>
            <dt className="text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">{t.reader.rights}</dt>
            <dd className="text-[var(--text-ink)]">{passage.rightsNote[lang]}</dd>
          </div>
          <div>
            <dt className="text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">{t.reader.reviewedBy}</dt>
            <dd className="text-[var(--text-ink)]">
              {t.featured.verifiedStatus} ({passage.verifiedDate})
            </dd>
          </div>
        </dl>
      </section>
    </div>
  );
};

