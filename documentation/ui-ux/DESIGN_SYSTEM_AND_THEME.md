# UI/UX Design System & Single-Place Theme Management

> **Status:** Approved Design Baseline (incorporating user overrides over initial blueprint placeholder tokens)

---

## 1. Core Aesthetic Direction: "Sacred Manuscript & Sandstone"

- **Feel:** Reverent, calm, editorial, trustworthy, deeply readable — like an archival spiritual library rather than a generic SaaS or AI landing page.
- **Single Unified Mixed Theme (NO Light/Dark Switch):**
  - Instead of separate light and dark modes, the platform uses **one unified theme** that naturally balances:
    1. **Warm Sunlit Reading Surfaces** (soft parchment & sandalwood ivory) so long scriptures, Hindi verses, and comparisons never strain the eyes.
    2. **Deep Grounded Umber / Rosewood Anchors** (top navigation masthead, scripture focus banners, primary quote panels, and footer) giving visual authority, depth, and effortless contrast without harsh `#000000` black or clinical `#FFFFFF` white.

---

## 2. Single-Place Theme Management Architecture

All visual tokens must be managed in **one central location**: `frontend/shared-theme/`.

```text
frontend/shared-theme/
├── tokens.css           # Master CSS custom properties (:root) — THE single source of truth
├── tailwind.preset.js   # Maps Tailwind semantic utilities directly to CSS variables
├── typography.css       # Font imports, Devanagari line-height & large reading scale
└── theme-rules.md       # Linting / usage contract
```

**Strict Rules:**
1. Both `frontend/public-website` and `frontend/admin-dashboard` import `shared-theme/tokens.css` and extend `shared-theme/tailwind.preset.js`.
2. **Zero hardcoded hex colors** inside feature components (`#...`, `bg-blue-*`, `bg-green-*`, `bg-black`, `bg-white`). Always use semantic tokens such as `bg-canvas`, `bg-surface`, `bg-anchor`, `bg-btn-primary`, `text-ink`, `border-sand`.
3. Changing a button color, reading background, or font size in `shared-theme/tokens.css` updates the entire platform at once.

---

## 3. Color System (Spiritual Palette — Zero Blue, Black, White, or Green Buttons)

### 3.1 Surfaces & Structural Zones (Mix of Warm Light + Grounded Deep)

| Token | Hex | Role |
|---|---|---|
| `--bg-canvas` | `#F6F1E7` | Primary warm parchment page background |
| `--bg-surface` | `#FCF9F2` | Reading cards, scripture panels, forms (warm ivory, never sterile `#FFFFFF`) |
| `--bg-surface-muted` | `#EDE3D2` | Secondary sandalwood surface, filter trays, comparison headers |
| `--bg-anchor` | `#241B15` | Deep rosewood-umber masthead, featured scripture banner, footer |
| `--bg-anchor-elevated` | `#32261E` | Nested panels inside dark anchor blocks (e.g., search bar in hero, dark quote card) |
| `--border-subtle` | `#DDD0BC` | Warm sandstone divider on light surfaces |
| `--border-anchor` | `#523E2D` | Warm bronze hairline divider inside dark anchor zones |

### 3.2 Typography Ink Tokens

| Token | Hex | Role |
|---|---|---|
| `--text-ink` | `#231A13` | Primary reading text on light surfaces (high contrast, warm espresso) |
| `--text-ink-muted` | `#5B4B3C` | Secondary metadata, dates, source breadcrumbs |
| `--text-on-anchor` | `#F7F0E4` | Primary text on `#241B15` dark anchor zones |
| `--text-on-anchor-muted` | `#D7C7B2` | Secondary text inside dark anchor zones |
| `--text-sacred-accent` | `#8E3711` | Highlighted verse markers, active links, editorial labels |

### 3.3 Button Tokens (Strictly Spiritual Tone)

> **Hard Prohibition:** No blue buttons, no black buttons, no pure white buttons, and no green buttons anywhere in Public or Admin UI.

| Button Variant | Background (`--btn-*`) | Hover | Text | Border | Usage |
|---|---|---|---|---|---|
| **Primary CTA** | `#A84313` (Sacred Saffron-Terracotta) | `#86320B` (Deep Kumkum) | `#FAF4E8` (Warm Cream) | `#86320B` | Ask AI, Search, Publish, Save, Submit |
| **Secondary Action** | `#E7D9C1` (Warm Sandalwood) | `#DAC7A8` | `#2B1E14` | `#B99768` (Antique Bronze) | Compare, Filter, View Source, Cancel |
| **Dark-Anchor CTA** | `#C8822E` (Burnished Marigold-Gold) | `#AD6D20` | `#21170F` | `#DF9E4B` | Primary actions inside dark `#241B15` headers/banners |
| **Outline / Ghost** | `transparent` | `#EFE5D4` | `#7D3B16` | `#C7AE8B` | Pagination, secondary tools, language switch |
| **Destructive / Reject** | `#8B261D` (Deep Sindoor Crimson) | `#6F1B14` | `#FAF4E8` | `#6F1B14` | Reject record, delete draft, flag violation |

### 3.4 Verification & Evidence Status Badges (Editorial, Non-Neon)

| Status / Meaning | Surface | Text | Border |
|---|---|---|---|
| `PUBLISHED` / `VERIFIED` | `#F5E9D0` (Warm Gold Wash) | `#7A4B0C` (Antique Amber-Bronze) | `#D4AF6A` |
| `NEEDS_REVIEW` / `PROCESSING` | `#EFE4D6` | `#6B4423` | `#C8A882` |
| `CONTRADICTING` / `REJECTED` | `#F7E4E1` | `#85251D` | `#D69C95` |
| `CONTEXTUAL` / `INCONCLUSIVE` | `#EAE4DC` | `#4E4237` | `#C2B6A7` |

---

## 4. Large, Accessible Typography Scale (No Small Text)

Designed so elders, scholars, and mobile visitors can read Hindi and English comfortably without zooming.

### 4.1 Font Families
- **English Editorial Headings:** `'Noto Serif', Georgia, serif`
- **English Body & UI Controls:** `'Inter', system-ui, sans-serif`
- **Hindi / Devanagari (Headings & Body):** `'Noto Sans Devanagari', sans-serif`

### 4.2 Type Scale (Minimum `16px` for Metadata, `18px` for Body, `20px+` for Scriptures)

| Token | Mobile (`320px+`) | Desktop (`1024px+`) | Line Height | Usage |
|---|---|---|---|---|
| `--text-hero` | `2rem` (`32px`) | `3rem` (`48px`) | `1.22` | Page & Hero headings |
| `--text-h1` | `1.625rem` (`26px`) | `2.25rem` (`36px`) | `1.28` | Section / Scripture titles |
| `--text-h2` | `1.375rem` (`22px`) | `1.75rem` (`28px`) | `1.34` | Subheadings & comparison headers |
| `--text-scripture` | `1.25rem` (`20px`) | `1.375rem` (`22px`) | `1.85` (`1.95` for Hindi) | Primary verse, quote & teaching blocks |
| `--text-body` | `1.125rem` (`18px`) | `1.1875rem` (`19px`) | `1.72` (`1.85` for Hindi) | Default prose, AI explanations, descriptions |
| `--text-ui` | `1.0625rem` (`17px`) | `1.0625rem` (`17px`) | `1.5` | Buttons, nav links, form inputs, tabs |
| `--text-meta` | `1rem` (`16px`) | `1rem` (`16px`) | `1.55` | Citations, page numbers, timestamps, badges |
| `--text-caption` | `0.9375rem` (`15px`) | `0.9375rem` (`15px`) | `1.5` | **Absolute floor** — never go below `15px` |

---

## 5. Layout, Touch & Component Rules

- **Mobile-First Breakpoints:** Test `320px`, `360px`, `390px`, `412px` first, then `768px` tablet and `1280px+` desktop.
- **Touch Targets:** Minimum `48px` height (`min-h-[48px]`) on all buttons, tabs, language switchers, and search inputs.
- **Reading Measure:** Limit prose & scripture columns to `62ch–68ch` for optimal eye tracking.
- **Anti-Slop Layout Rules:**
  - Vary visual rhythm across sections (dark editorial hero masthead → featured primary manuscript reader strip → structured thematic index → evidence-backed comparison showcase → calm contribution bar).
  - Never stack 4 identical gradient SaaS cards everywhere.
  - No glassmorphism blobs, no neon gradients, no cluttered micro-badges.
  - Comparison tables automatically transform into clearly labeled vertical perspective stacks on mobile (`< 768px`).

---

## 6. Signature Screen Structure

1. **Public Homepage:**
   - Deep `#241B15` masthead with bilingual switcher (`EN / हिन्दी`), clear search bar (full-width on mobile), and Saffron-Terracotta Search CTA.
   - Editorial Featured Evidence Strip showing an authentic Scripture Passage alongside its Attribution, Printed Page vs. PDF Page, and Verified Status.
   - Direct pathways into **Scripture Library**, **Teachings**, **Comparative Research**, **Ask AI (Evidence-Grounded)**, and **Video Segments**.
2. **Scripture / Source Detail Reader:**
   - Sticky reference breadcrumb (e.g., *Rigveda → Mandal 10 → Sukta 90 → Mantra 1* or *Book → Edition → Printed p. 42 / PDF p. 48*).
   - Large `20px–22px` Original Text panel + cleanly separated Published Translation tab/block + Commentary/Context accordion.
   - Verification & Rights footer showing exact edition, publisher, license status, and reviewer timestamp.
3. **Comparative Research View:**
   - Three distinct numbered lanes (stacked on mobile, structured columns on desktop):
     1. Original Scripture / Primary Source
     2. Other Saints / Translators / Commentators
     3. Teachings Attributed to Sant Rampal Ji Maharaj
   - Explicit finding pills: *Agreement*, *Translation Difference*, *Interpretation Difference*, *Contradiction*, *Missing Context*, or *Inconclusive*.
4. **Ask AI Page:**
   - Prominent disclaimer banner: *"Answers are generated only from verified, published platform records. Every claim links to primary evidence."*
   - Two-part response layout: **(A)** Grounded Explanation with inline evidence markers `[E1]`, followed by **(B)** Expandable Primary Source Cards containing exact quoted text, edition, page/timestamp, and verification badge.

