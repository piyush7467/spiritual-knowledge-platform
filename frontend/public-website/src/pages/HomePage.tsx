import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, BookText, Scale, Sparkles, CheckCircle2 } from 'lucide-react';
import { Language, translations } from '@/i18n/translations';
import { SAMPLE_PASSAGES } from '@/data/sampleArchive';
import { SpiritualButton } from '@/components/ui/SpiritualButton';
import { StatusBadge } from '@/components/ui/StatusBadge';

interface HomePageProps {
  lang: Language;
}

export const HomePage: React.FC<HomePageProps> = ({ lang }) => {
  const [query, setQuery] = useState('');
  const navigate = useNavigate();
  const t = translations[lang];
  const featured = SAMPLE_PASSAGES[0];

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    navigate(`/search?q=${encodeURIComponent(query.trim())}`);
  };

  return (
    <div>
      {/* 1. Deep Anchor Hero Band — High Authority & Calm Search Focus */}
      <section className="bg-[var(--bg-anchor)] text-[var(--text-on-anchor)] border-b border-[var(--border-anchor)]">
        <div className="mx-auto max-w-6xl px-4 py-12 sm:px-6 sm:py-16 lg:py-20">
          <div className="max-w-3xl">
            <h1 className="font-editorial text-[length:var(--text-hero)] font-semibold leading-tight text-[var(--text-on-anchor)]">
              {t.hero.headline}
            </h1>
            <p className="mt-5 text-[length:var(--text-body)] leading-relaxed text-[var(--text-on-anchor-muted)]">
              {t.hero.subhead}
            </p>
          </div>

          <form
            onSubmit={handleSearch}
            className="mt-8 flex flex-col gap-3 rounded-lg border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] p-3 sm:flex-row sm:items-center"
          >
            <label htmlFor="home-search-input" className="sr-only">
              {t.hero.searchPlaceholder}
            </label>
            <div className="relative flex-1">
              <Search
                className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[var(--text-on-anchor-muted)]"
                aria-hidden="true"
              />
              <input
                id="home-search-input"
                type="search"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder={t.hero.searchPlaceholder}
                className="min-h-[52px] w-full rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor)] pl-12 pr-4 text-[length:var(--text-ui)] text-[var(--text-on-anchor)] placeholder:text-[var(--text-on-anchor-muted)]"
              />
            </div>
            <SpiritualButton type="submit" variant="primary" className="sm:shrink-0">
              {t.hero.searchAction}
            </SpiritualButton>
            <SpiritualButton
              type="button"
              variant="anchor"
              onClick={() => navigate('/search')}
              className="sm:shrink-0"
            >
              {t.hero.browseScriptures}
            </SpiritualButton>
          </form>
        </div>
      </section>

      {/* 2. Featured Verified Manuscript Stage (Warm Sunlit Parchment Reading Surface) */}
      <section className="mx-auto max-w-6xl px-4 py-12 sm:px-6 sm:py-16">
        <div className="flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h2 className="font-editorial text-[length:var(--text-h1)] font-semibold text-[var(--text-ink)]">
              {t.featured.sectionTitle}
            </h2>
            <p className="mt-1 text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">
              {featured.sourceTitle[lang]} · {featured.referenceHierarchy[lang]}
            </p>
          </div>
          <StatusBadge tone="verified" label={t.featured.verifiedStatus} />
        </div>

        <article className="mt-6 rounded-lg border border-[var(--border-strong)] bg-[var(--bg-surface)] p-6 shadow-card sm:p-8">
          <div className="grid gap-8 lg:grid-cols-12">
            {/* Original Scripture Text */}
            <div className="lg:col-span-7 lg:border-r lg:border-[var(--border-subtle)] lg:pr-8">
              <span className="inline-block text-[length:var(--text-meta)] font-semibold text-[var(--text-sacred-accent)]">
                {t.featured.originalTextLabel} ({featured.originalLanguageLabel[lang]})
              </span>
              <p
                lang="hi"
                className="scripture-prose mt-3 rounded-md border border-[var(--border-subtle)] bg-[var(--bg-canvas)] p-5 font-medium text-[var(--text-ink)]"
              >
                {featured.originalScript}
              </p>

              <span className="mt-6 inline-block text-[length:var(--text-meta)] font-semibold text-[var(--text-ink-muted)]">
                {t.featured.translationLabel}
              </span>
              <p className="mt-2 text-[length:var(--text-body)] leading-relaxed text-[var(--text-ink)]">
                {featured.publishedTranslation[lang]}
              </p>
            </div>

            {/* Provenance & Editorial Layer Separation */}
            <div className="flex flex-col justify-between gap-6 lg:col-span-5">
              <div className="space-y-4">
                <h3 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
                  {t.featured.interpretationLabel}
                </h3>
                <p className="text-[length:var(--text-body)] text-[var(--text-ink)]">
                  {featured.attributionContext[lang]}
                </p>

                <dl className="grid grid-cols-2 gap-3 rounded-md border border-[var(--border-subtle)] bg-[var(--bg-surface-muted)] p-4 text-[length:var(--text-meta)]">
                  <div>
                    <dt className="text-[var(--text-ink-muted)]">{t.featured.printedPage}</dt>
                    <dd className="font-semibold text-[var(--text-ink)]">{featured.printedPage}</dd>
                  </div>
                  <div>
                    <dt className="text-[var(--text-ink-muted)]">{t.featured.pdfPage}</dt>
                    <dd className="font-semibold text-[var(--text-ink)]">{featured.pdfPage}</dd>
                  </div>
                </dl>
              </div>

              <div className="flex flex-wrap gap-3">
                <Link to={`/scriptures/${featured.publicId}`} className="w-full sm:w-auto">
                  <SpiritualButton variant="primary" fullWidth>
                    {t.featured.openDetail}
                  </SpiritualButton>
                </Link>
                <Link to="/compare" className="w-full sm:w-auto">
                  <SpiritualButton variant="secondary" fullWidth>
                    {t.nav.compare}
                  </SpiritualButton>
                </Link>
              </div>
            </div>
          </div>
        </article>
      </section>

      {/* 3. Research Pathways (Varied Editorial List — Not Generic 4-Card Grid) */}
      <section className="border-y border-[var(--border-subtle)] bg-[var(--bg-surface-muted)]">
        <div className="mx-auto max-w-6xl px-4 py-12 sm:px-6 sm:py-16">
          <h2 className="font-editorial text-[length:var(--text-h1)] font-semibold text-[var(--text-ink)]">
            {t.pathways.title}
          </h2>

          <div className="mt-8 divide-y divide-[var(--border-strong)] rounded-lg border border-[var(--border-subtle)] bg-[var(--bg-surface)]">
            <div className="flex flex-col gap-4 p-6 sm:flex-row sm:items-center sm:justify-between">
              <div className="flex items-start gap-4">
                <BookText className="mt-1 h-7 w-7 shrink-0 text-[var(--text-sacred-accent)]" aria-hidden="true" />
                <div className="max-w-2xl">
                  <h3 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
                    {t.pathways.scriptureTitle}
                  </h3>
                  <p className="mt-1 text-[length:var(--text-body)] text-[var(--text-ink-muted)]">
                    {t.pathways.scriptureDesc}
                  </p>
                </div>
              </div>
              <Link to="/search" className="sm:shrink-0">
                <SpiritualButton variant="secondary">{t.nav.scriptures}</SpiritualButton>
              </Link>
            </div>

            <div className="flex flex-col gap-4 p-6 sm:flex-row sm:items-center sm:justify-between">
              <div className="flex items-start gap-4">
                <Scale className="mt-1 h-7 w-7 shrink-0 text-[var(--text-sacred-accent)]" aria-hidden="true" />
                <div className="max-w-2xl">
                  <h3 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
                    {t.pathways.compareTitle}
                  </h3>
                  <p className="mt-1 text-[length:var(--text-body)] text-[var(--text-ink-muted)]">
                    {t.pathways.compareDesc}
                  </p>
                </div>
              </div>
              <Link to="/compare" className="sm:shrink-0">
                <SpiritualButton variant="secondary">{t.nav.compare}</SpiritualButton>
              </Link>
            </div>

            <div className="flex flex-col gap-4 p-6 sm:flex-row sm:items-center sm:justify-between">
              <div className="flex items-start gap-4">
                <Sparkles className="mt-1 h-7 w-7 shrink-0 text-[var(--text-sacred-accent)]" aria-hidden="true" />
                <div className="max-w-2xl">
                  <h3 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
                    {t.pathways.askAiTitle}
                  </h3>
                  <p className="mt-1 text-[length:var(--text-body)] text-[var(--text-ink-muted)]">
                    {t.pathways.askAiDesc}
                  </p>
                </div>
              </div>
              <Link to="/ask" className="sm:shrink-0">
                <SpiritualButton variant="primary">{t.nav.askAi}</SpiritualButton>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* 4. Editorial Verification Standards */}
      <section className="mx-auto max-w-6xl px-4 py-12 sm:px-6 sm:py-16">
        <div className="rounded-lg border border-[var(--border-anchor)] bg-[var(--bg-anchor)] p-6 text-[var(--text-on-anchor)] sm:p-10">
          <h2 className="font-editorial text-[length:var(--text-h1)] font-semibold">
            {t.principles.title}
          </h2>
          <p className="mt-2 max-w-2xl text-[length:var(--text-body)] text-[var(--text-on-anchor-muted)]">
            {t.principles.subtitle}
          </p>
          <ul className="mt-6 space-y-4">
            {t.principles.items.map((item) => (
              <li key={item} className="flex items-start gap-3 text-[length:var(--text-body)]">
                <CheckCircle2 className="mt-1 h-6 w-6 shrink-0 text-[var(--btn-anchor-bg)]" aria-hidden="true" />
                <span>{item}</span>
              </li>
            ))}
          </ul>
        </div>
      </section>
    </div>
  );
};

