import React, { useMemo, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { Search } from 'lucide-react';
import { Language, translations } from '@/i18n/translations';
import { SAMPLE_PASSAGES } from '@/data/sampleArchive';
import { SpiritualButton } from '@/components/ui/SpiritualButton';
import { StatusBadge } from '@/components/ui/StatusBadge';

interface SearchPageProps {
  lang: Language;
}

export const SearchPage: React.FC<SearchPageProps> = ({ lang }) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const initialQuery = searchParams.get('q') ?? '';
  const [query, setQuery] = useState(initialQuery);
  const [selectedSource, setSelectedSource] = useState<string>('ALL');
  const t = translations[lang];

  const sourceOptions = useMemo(() => {
    const unique = Array.from(new Set(SAMPLE_PASSAGES.map((p) => p.sourceTitle[lang])));
    return ['ALL', ...unique];
  }, [lang]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return SAMPLE_PASSAGES.filter((item) => {
      const matchesSource =
        selectedSource === 'ALL' || item.sourceTitle[lang] === selectedSource;
      if (!matchesSource) return false;
      if (!q) return true;
      return (
        item.sourceTitle.en.toLowerCase().includes(q) ||
        item.sourceTitle.hi.toLowerCase().includes(q) ||
        item.referenceHierarchy.en.toLowerCase().includes(q) ||
        item.referenceHierarchy.hi.toLowerCase().includes(q) ||
        item.canonicalRef.toLowerCase().includes(q) ||
        item.originalScript.toLowerCase().includes(q) ||
        item.publishedTranslation.en.toLowerCase().includes(q) ||
        item.publishedTranslation.hi.toLowerCase().includes(q)
      );
    });
  }, [query, selectedSource, lang]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setSearchParams(query.trim() ? { q: query.trim() } : {});
  };

  return (
    <div className="mx-auto max-w-6xl px-4 py-10 sm:px-6 sm:py-14">
      <div className="rounded-lg border border-[var(--border-anchor)] bg-[var(--bg-anchor)] p-6 text-[var(--text-on-anchor)] sm:p-8">
        <h1 className="font-editorial text-[length:var(--text-h1)] font-semibold">
          {t.nav.scriptures}
        </h1>
        <form onSubmit={handleSubmit} className="mt-5 flex flex-col gap-3 sm:flex-row">
          <div className="relative flex-1">
            <Search
              className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-[var(--text-on-anchor-muted)]"
              aria-hidden="true"
            />
            <input
              type="search"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder={t.hero.searchPlaceholder}
              className="min-h-[52px] w-full rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] pl-12 pr-4 text-[length:var(--text-ui)] text-[var(--text-on-anchor)] placeholder:text-[var(--text-on-anchor-muted)]"
            />
          </div>
          <SpiritualButton type="submit" variant="primary">
            {t.hero.searchAction}
          </SpiritualButton>
        </form>

        <div className="mt-5 flex flex-wrap gap-2">
          {sourceOptions.map((src) => {
            const active = selectedSource === src;
            return (
              <SpiritualButton
                key={src}
                type="button"
                variant={active ? 'anchor' : 'secondary'}
                onClick={() => setSelectedSource(src)}
              >
                {src === 'ALL' ? (lang === 'en' ? 'All Sources' : 'सभी ग्रंथ') : src}
              </SpiritualButton>
            );
          })}
        </div>
      </div>

      {/* Results List */}
      <div className="mt-8 space-y-6">
        {filtered.length === 0 ? (
          <div className="rounded-lg border border-[var(--border-subtle)] bg-[var(--bg-surface)] p-8 text-center">
            <p className="text-[length:var(--text-body)] text-[var(--text-ink)]">
              {lang === 'en'
                ? 'No verified passages match that query yet. Try clearing filters or searching Rigveda, Gita, or Ang 721.'
                : 'इस खोज के लिए अभी कोई प्रमाणित प्रसंग नहीं मिला। कृपया फ़िल्टर हटाएँ या ऋग्वेद, गीता, अंग 721 खोजें।'}
            </p>
          </div>
        ) : (
          filtered.map((item) => (
            <article
              key={item.publicId}
              className="rounded-lg border border-[var(--border-subtle)] bg-[var(--bg-surface)] p-6 shadow-card"
            >
              <div className="flex flex-wrap items-start justify-between gap-4">
                <div>
                  <p className="text-[length:var(--text-meta)] text-[var(--text-sacred-accent)] font-semibold">
                    {item.sourceTitle[lang]} · {item.referenceHierarchy[lang]}
                  </p>
                  <p className="mt-1 text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">
                    {t.featured.printedPage} {item.printedPage} · {t.featured.pdfPage} {item.pdfPage}
                  </p>
                </div>
                <StatusBadge tone="verified" label={t.featured.verifiedStatus} />
              </div>

              <p
                lang="hi"
                className="scripture-prose mt-4 rounded-md border border-[var(--border-subtle)] bg-[var(--bg-canvas)] p-4 text-[var(--text-ink)]"
              >
                {item.originalScript}
              </p>

              <p className="mt-4 text-[length:var(--text-body)] text-[var(--text-ink)]">
                {item.publishedTranslation[lang]}
              </p>

              <div className="mt-5">
                <Link to={`/scriptures/${item.publicId}`}>
                  <SpiritualButton variant="secondary">{t.featured.openDetail}</SpiritualButton>
                </Link>
              </div>
            </article>
          ))
        )}
      </div>
    </div>
  );
};

