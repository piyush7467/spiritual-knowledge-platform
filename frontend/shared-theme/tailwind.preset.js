/**
 * Single-Place Tailwind Preset
 * Shared across frontend/public-website and frontend/admin-dashboard.
 * Every color and typography token reads directly from shared-theme/tokens.css.
 */
export default {
  theme: {
    extend: {
      colors: {
        canvas: 'var(--bg-canvas)',
        surface: {
          DEFAULT: 'var(--bg-surface)',
          muted: 'var(--bg-surface-muted)',
        },
        anchor: {
          DEFAULT: 'var(--bg-anchor)',
          elevated: 'var(--bg-anchor-elevated)',
        },
        border: {
          subtle: 'var(--border-subtle)',
          strong: 'var(--border-strong)',
          anchor: 'var(--border-anchor)',
        },
        ink: {
          DEFAULT: 'var(--text-ink)',
          muted: 'var(--text-ink-muted)',
          onAnchor: 'var(--text-on-anchor)',
          onAnchorMuted: 'var(--text-on-anchor-muted)',
          sacred: 'var(--text-sacred-accent)',
        },
        btn: {
          primary: 'var(--btn-primary-bg)',
          primaryHover: 'var(--btn-primary-hover)',
          primaryText: 'var(--btn-primary-text)',
          secondary: 'var(--btn-secondary-bg)',
          secondaryHover: 'var(--btn-secondary-hover)',
          secondaryText: 'var(--btn-secondary-text)',
          anchor: 'var(--btn-anchor-bg)',
          anchorHover: 'var(--btn-anchor-hover)',
          anchorText: 'var(--btn-anchor-text)',
          danger: 'var(--btn-danger-bg)',
          dangerHover: 'var(--btn-danger-hover)',
        },
        badge: {
          verifiedBg: 'var(--badge-verified-bg)',
          verifiedText: 'var(--badge-verified-text)',
          verifiedBorder: 'var(--badge-verified-border)',
          reviewBg: 'var(--badge-review-bg)',
          reviewText: 'var(--badge-review-text)',
          reviewBorder: 'var(--badge-review-border)',
          alertBg: 'var(--badge-alert-bg)',
          alertText: 'var(--badge-alert-text)',
          alertBorder: 'var(--badge-alert-border)',
          neutralBg: 'var(--badge-neutral-bg)',
          neutralText: 'var(--badge-neutral-text)',
          neutralBorder: 'var(--badge-neutral-border)',
        },
      },
      fontFamily: {
        serif: ['var(--font-serif)'],
        sans: ['var(--font-sans)'],
        devanagari: ['var(--font-devanagari)'],
      },
      fontSize: {
        hero: ['var(--text-hero)', { lineHeight: '1.22' }],
        h1: ['var(--text-h1)', { lineHeight: '1.28' }],
        h2: ['var(--text-h2)', { lineHeight: '1.34' }],
        scripture: ['var(--text-scripture)', { lineHeight: '1.88' }],
        body: ['var(--text-body)', { lineHeight: '1.74' }],
        ui: ['var(--text-ui)', { lineHeight: '1.5' }],
        meta: ['var(--text-meta)', { lineHeight: '1.55' }],
        floor: ['var(--text-floor)', { lineHeight: '1.5' }],
      },
      borderRadius: {
        sm: 'var(--radius-sm)',
        md: 'var(--radius-md)',
        lg: 'var(--radius-lg)',
      },
      boxShadow: {
        card: 'var(--shadow-card)',
        anchor: 'var(--shadow-anchor)',
      },
    },
  },
};

