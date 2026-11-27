import React, { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { BookOpen, Menu, X, Languages } from 'lucide-react';
import { Language, translations } from '@/i18n/translations';
import { SpiritualButton } from '@/components/ui/SpiritualButton';

interface SiteShellProps {
  lang: Language;
  onToggleLang: () => void;
  children: React.ReactNode;
}

export const SiteShell: React.FC<SiteShellProps> = ({ lang, onToggleLang, children }) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const t = translations[lang];

  const navItems = [
    { to: '/', label: t.nav.home },
    { to: '/search', label: t.nav.scriptures },
    { to: '/compare', label: t.nav.compare },
    { to: '/ask', label: t.nav.askAi },
  ];

  return (
    <div lang={lang} className="min-h-screen flex flex-col bg-[var(--bg-canvas)] text-[var(--text-ink)]">
      {/* Deep Rosewood-Umber Spiritual Masthead (Dark Anchor Zone) */}
      <header className="sticky top-0 z-40 border-b border-[var(--border-anchor)] bg-[var(--bg-anchor)] text-[var(--text-on-anchor)] shadow-anchor">
        <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 py-3.5 sm:px-6">
          <Link to="/" className="flex min-h-[48px] items-center gap-3 focus-visible:outline-none">
            <span className="flex h-11 w-11 shrink-0 items-center justify-center rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] text-[var(--btn-anchor-bg)]">
              <BookOpen className="h-6 w-6" aria-hidden="true" />
            </span>
            <div className="leading-tight">
              <span className="block font-editorial text-xl font-semibold tracking-tight text-[var(--text-on-anchor)]">
                {t.siteName}
              </span>
              <span className="hidden text-[length:var(--text-floor)] text-[var(--text-on-anchor-muted)] sm:block">
                {t.siteTagline}
              </span>
            </div>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden items-center gap-2 lg:flex" aria-label="Primary Navigation">
            {navItems.map((item) => (
              <NavLink
                key={item.to}
                to={item.to}
                className={({ isActive }) =>
                  `min-h-[48px] inline-flex items-center rounded-md px-4 py-2 text-[length:var(--text-ui)] font-medium transition-colors ${
                    isActive
                      ? 'bg-[var(--bg-anchor-elevated)] text-[var(--btn-anchor-bg)] border border-[var(--border-anchor)]'
                      : 'text-[var(--text-on-anchor)] hover:bg-[var(--bg-anchor-elevated)]'
                  }`
                }
              >
                {item.label}
              </NavLink>
            ))}
          </nav>

          {/* Bilingual Toggle + Mobile Menu Button */}
          <div className="flex items-center gap-2">
            <SpiritualButton
              type="button"
              variant="anchor"
              onClick={onToggleLang}
              aria-label={lang === 'en' ? 'Switch language to Hindi' : 'भाषा अंग्रेज़ी में बदलें'}
              className="px-4"
            >
              <Languages className="h-5 w-5" aria-hidden="true" />
              <span>{lang === 'en' ? 'हिन्दी' : 'English'}</span>
            </SpiritualButton>

            <button
              type="button"
              onClick={() => setMenuOpen((prev) => !prev)}
              aria-expanded={menuOpen}
              aria-label="Toggle Navigation Menu"
              className="inline-flex min-h-[48px] min-w-[48px] items-center justify-center rounded-md border border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] text-[var(--text-on-anchor)] lg:hidden"
            >
              {menuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
            </button>
          </div>
        </div>

        {/* Mobile Drawer */}
        {menuOpen && (
          <nav
            className="border-t border-[var(--border-anchor)] bg-[var(--bg-anchor-elevated)] px-4 py-4 lg:hidden"
            aria-label="Mobile Navigation"
          >
            <div className="flex flex-col gap-2">
              {navItems.map((item) => (
                <NavLink
                  key={item.to}
                  to={item.to}
                  onClick={() => setMenuOpen(false)}
                  className={({ isActive }) =>
                    `min-h-[48px] flex items-center rounded-md px-4 py-3 text-[length:var(--text-ui)] font-medium ${
                      isActive
                        ? 'bg-[var(--btn-primary-bg)] text-[var(--btn-primary-text)]'
                        : 'text-[var(--text-on-anchor)] hover:bg-[var(--bg-anchor)]'
                    }`
                  }
                >
                  {item.label}
                </NavLink>
              ))}
            </div>
          </nav>
        )}
      </header>

      <main className="flex-1">{children}</main>

      {/* Deep Anchor Footer */}
      <footer className="border-t border-[var(--border-anchor)] bg-[var(--bg-anchor)] text-[var(--text-on-anchor)]">
        <div className="mx-auto max-w-6xl px-4 py-10 sm:px-6">
          <div className="flex flex-col gap-6 md:flex-row md:items-center md:justify-between">
            <div className="max-w-xl">
              <p className="font-editorial text-xl font-semibold text-[var(--text-on-anchor)]">
                {t.siteName}
              </p>
              <p className="mt-2 text-[length:var(--text-meta)] text-[var(--text-on-anchor-muted)]">
                {t.footer.note}
              </p>
            </div>
            <div className="flex flex-wrap gap-4 text-[length:var(--text-meta)] text-[var(--text-on-anchor-muted)]">
              <Link to="/compare" className="underline underline-offset-4 hover:text-[var(--btn-anchor-bg)]">
                {t.footer.methodology}
              </Link>
              <Link to="/ask" className="underline underline-offset-4 hover:text-[var(--btn-anchor-bg)]">
                {t.nav.askAi}
              </Link>
              <span>{t.footer.copyright}</span>
              <span>{t.footer.privacy}</span>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
};

