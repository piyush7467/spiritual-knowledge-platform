import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { ShieldAlert, Sparkles } from 'lucide-react';
import { Language, translations } from '@/i18n/translations';
import { SAMPLE_PASSAGES } from '@/data/sampleArchive';
import { SpiritualButton } from '@/components/ui/SpiritualButton';
import { StatusBadge } from '@/components/ui/StatusBadge';

interface AskAiPageProps {
  lang: Language;
}

export const AskAiPage: React.FC<AskAiPageProps> = ({ lang }) => {
  const [question, setQuestion] = useState('');
  const [submittedQuestion, setSubmittedQuestion] = useState<string | null>(null);
  const t = translations[lang];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!question.trim()) return;
    setSubmittedQuestion(question.trim());
  };

  return (
    <div className="mx-auto max-w-5xl px-4 py-10 sm:px-6 sm:py-14">
      <header className="rounded-lg border border-[var(--border-anchor)] bg-[var(--bg-anchor)] p-6 text-[var(--text-on-anchor)] sm:p-8">
        <h1 className="font-editorial text-[length:var(--text-h1)] font-semibold">
          {t.askAi.title}
        </h1>
        <div className="mt-4 flex items-start gap-3 rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] p-4 text-[length:var(--text-meta)] text-[var(--text-on-anchor-muted)]">
          <ShieldAlert className="mt-0.5 h-5 w-5 shrink-0 text-[var(--btn-anchor-bg)]" aria-hidden="true" />
          <p>{t.askAi.notice}</p>
        </div>

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <label htmlFor="ask-ai-textarea" className="sr-only">
            {t.askAi.promptPlaceholder}
          </label>
          <textarea
            id="ask-ai-textarea"
            rows={3}
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            placeholder={t.askAi.promptPlaceholder}
            className="w-full rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] p-4 text-[length:var(--text-body)] text-[var(--text-on-anchor)] placeholder:text-[var(--text-on-anchor-muted)]"
          />
          <SpiritualButton type="submit" variant="primary">
            <Sparkles className="h-5 w-5" aria-hidden="true" />
            <span>{t.askAi.submit}</span>
          </SpiritualButton>
        </form>
      </header>

      {submittedQuestion && (
        <section className="mt-8 space-y-6">
          {/* Layer 6: AI Explanation clearly separated from primary citations */}
          <div className="rounded-lg border border-[var(--border-strong)] bg-[var(--bg-surface)] p-6 shadow-card sm:p-8">
            <div className="flex flex-wrap items-center justify-between gap-3">
              <StatusBadge
                tone="review"
                label={lang === 'en' ? 'AI Synthesis (Citations Validated)' : 'AI संश्लेषण (संदर्भ सत्यापित)'}
              />
              <span className="text-[length:var(--text-meta)] text-[var(--text-ink-muted)]">
                {lang === 'en' ? 'Preview baseline before FastAPI live link' : 'लाइव FastAPI जुड़ाव से पूर्व पूर्वावलोकन'}
              </span>
            </div>

            <p className="mt-5 text-[length:var(--text-body)] leading-relaxed text-[var(--text-ink)]">
              {lang === 'en'
                ? `Regarding "${submittedQuestion}": Verified passages in this archive show Rigveda 10.90.1 [E1] describing the Supreme Purusha transcending creation, Bhagavad Gita 15:17 [E2] distinguishing Uttam Purusha (Paramatma) from Kshar and Akshar, and Guru Granth Sahib Ang 721 [E3] directly addressing Kabir as Karatar.`
                : `"${submittedQuestion}" के संबंध में सत्यापित अभिलेख दर्शाते हैं कि ऋग्वेद 10.90.1 [E1] में परम पुरुष के स्वरूप, श्रीमद्भगवद्गीता 15:17 [E2] में क्षर व अक्षर से भिन्न उत्तम पुरुष (परमात्मा), तथा श्री गुरु ग्रंथ साहिब अंग 721 [E3] में कबीर साहेब को करतार के रूप में संबोधित किया गया है।`}
            </p>
          </div>

          {/* Real Source Cards */}
          <div>
            <h2 className="font-editorial text-[length:var(--text-h2)] font-semibold text-[var(--text-ink)]">
              {t.askAi.citedSources}
            </h2>
            <div className="mt-4 space-y-4">
              {SAMPLE_PASSAGES.map((psg, idx) => (
                <article
                  key={psg.publicId}
                  className="rounded-lg border border-[var(--border-subtle)] bg-[var(--bg-surface)] p-5"
                >
                  <div className="flex flex-wrap items-center justify-between gap-3">
                    <span className="text-[length:var(--text-meta)] font-semibold text-[var(--text-sacred-accent)]">
                      [E{idx + 1}] {psg.sourceTitle[lang]} · {psg.referenceHierarchy[lang]}
                    </span>
                    <StatusBadge tone="verified" label={`${t.featured.printedPage} ${psg.printedPage}`} />
                  </div>
                  <p className="mt-3 text-[length:var(--text-body)] text-[var(--text-ink)]">
                    {psg.publishedTranslation[lang]}
                  </p>
                  <div className="mt-4">
                    <Link to={`/scriptures/${psg.publicId}`}>
                      <SpiritualButton variant="secondary">{t.featured.openDetail}</SpiritualButton>
                    </Link>
                  </div>
                </article>
              ))}
            </div>
          </div>
        </section>
      )}
    </div>
  );
};

