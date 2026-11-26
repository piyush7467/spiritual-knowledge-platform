# Spiritual Knowledge & Evidence Platform — Master Execution Plan (`min.md`)

> **Purpose of this file:** Persistent single source of truth for the entire project design, step-by-step build sequence, non-negotiable rules, and live progress tracking so context is never lost across sessions.

---

## 1. Never-Forget Context Anchor

### 1.1 Core Mission & Truthfulness Rules
- **Mission:** Public spiritual knowledge & evidence platform centered on the teachings of **Sant Rampal Ji Maharaj** alongside original scriptures, published translations, saints, commentators, and traceable sources.
- **Zero-Fabrication Rule:** Every important published claim, quotation, comparison, and AI answer **must** trace to verified source records. AI must **never** invent quotations, verse numbers, page numbers, URLs, timestamps, or attributions.
- **6 Distinct Knowledge Layers (Never Mix):**
  1. Original source text
  2. Published translation
  3. Translator/commentator interpretation
  4. Statement attributed to a teacher/author
  5. Editorial research summary
  6. AI-generated explanation
- **Evidence State Machine:** `DISCOVERED` → `REGISTERED` → `PROCESSING` → `NEEDS_REVIEW` → `VERIFIED` → `PUBLISHED` (plus `NEEDS_CORRECTION`, `REJECTED`, `ARCHIVED`). Only `PUBLISHED` records enter public display and production AI RAG retrieval.

### 1.2 UI/UX & Single-Place Theme Rules (User-Specified Override)
- **One-Place Theme Management:** All colors, typography, spacing, borders, radii, and elevation live in **one central token source** shared by both `public-website` and `admin-dashboard`. No scattered hardcoded hex colors or per-component styling overrides.
- **Single Unified Mixed Theme (NO Light/Dark Toggle):** One calm, authoritative, spiritual theme that blends **warm sunlit parchment reading surfaces** with **deep grounded umber/rosewood structural anchors** (header, hero manuscript banners, quote callouts, footer) — harmonious, never harsh `#000000` black or clinical `#FFFFFF` white.
- **Spiritual Tone Buttons (NO Blue, Black, White, or Green Buttons):**
  - Primary CTA button: **Sacred Saffron-Terracotta (`#A84313`)** with warm ivory text (`#FAF4E8`), hover **Deep Kumkum-Bronze (`#86320B`)**.
  - Secondary button: **Warm Sandalwood-Gold (`#E7D9C1`)** with deep espresso text (`#2B1E14`) and antique bronze border (`#B99768`).
  - Subtle/Ghost action: Transparent with `#7D3B16` text and warm sandstone hover (`#EFE5D4`).
- **Large, Effortlessly Readable Typography (NO Small Text):**
  - Body text minimum **`18px`** (`1.125rem`); long-form scripture & teaching reading **`20px–21px`** (`1.25rem–1.3125rem`).
  - Metadata, badges, helper labels, and captions minimum **`15px–16px`** (`0.9375rem–1rem`) — avoid `11px–13px` micro-text.
  - Fonts: `Noto Serif` (English editorial headings), `Inter` (English UI/body), `Noto Sans Devanagari` (Hindi UI & scriptures with `1.85–1.95` line-height for crystal-clear matras).
  - Mobile-first (`320px–430px` first), minimum `48px` touch targets, zero horizontal overflow, calm editorial layout (no generic AI SaaS card grids or neon gradients).

### 1.3 Fixed Technical Stack
- **Frontend:** React + TypeScript + Vite + Tailwind CSS + `shadcn/ui` + Lucide React + React Router + TanStack Query + React Hook Form + Zod (`frontend/public-website`, `frontend/admin-dashboard`).
- **Main Backend:** Java 21 + Spring Boot (Modular Monolith — 15 modules communicating strictly via service interfaces, never cross-module repository calls) + Spring Security (JWT + RBAC) + Spring Data JPA + Flyway + Springdoc OpenAPI.
- **AI Service:** Python + FastAPI (`ai-service/`) behind a provider adapter (Gemini / OpenAI / Local model), PyMuPDF/Tika + OCR, hybrid full-text + `pgvector` retrieval, strict backend evidence-ID citation validation.
- **Database & Media:** Neon PostgreSQL (`pgvector` + FTS) + Cloudinary (signed backend uploads).
- **Security Rule:** Zero secrets in code, prompts, docs, seed data, or `VITE_*` variables. Only `.env.example` files committed.

---

## 2. Documentation Index (Project `.md` Files)

| Document | Purpose | Path |
|---|---|---|
| Project Baseline | Original product & technical blueprint | [spiritual_knowledge_platform_project_blueprint.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/spiritual_knowledge_platform_project_blueprint.md) |
| Master Plan & Context | All steps & persistent design memory | [min.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/min.md) |
| Project Overview | Root repo guide & quickstart | [README.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/README.md) |
| UI/UX & Theme Spec | Single-place spiritual theme, large type scale, page wireframes | [DESIGN_SYSTEM_AND_THEME.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/documentation/ui-ux/DESIGN_SYSTEM_AND_THEME.md) |
| System Architecture | Spring modular monolith + FastAPI AI service boundaries | [SYSTEM_ARCHITECTURE.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/documentation/architecture/SYSTEM_ARCHITECTURE.md) |
| Database & ERD | Complete tables, constraints, indexes, Flyway plan | [DATABASE_SCHEMA_AND_ERD.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/documentation/database/DATABASE_SCHEMA_AND_ERD.md) |
| API Contracts | Public, Auth, Admin, and Internal AI REST endpoints | [API_CONTRACTS.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/documentation/api/API_CONTRACTS.md) |
| Editorial & Workflow | Evidence rules, 9-state workflow, copyright & Hindi/EN rules | [EDITORIAL_AND_WORKFLOW_RULES.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/documentation/requirements/EDITORIAL_AND_WORKFLOW_RULES.md) |
| Ops & Environments | `.env.example` templates, security controls, deployment | [ENVIRONMENT_AND_DEPLOYMENT.md](file:///d:/WEB_PROJECTS/Spring/SpiritualKnowledgePlatform/documentation/operations/ENVIRONMENT_AND_DEPLOYMENT.md) |

---

## 3. Complete Step-by-Step Design & Build Plan (18 Steps Across 7 Phases)

### Phase 0 — Foundation & Design System (Steps 1–4)
- [x] **Step 1: Project Documentation & Master Plan (`min.md` + `documentation/`)**
  - Lock architecture, database ERD, API contracts, editorial rules, environment rules, and single-place Spiritual UI/UX theme spec.
- [ ] **Step 2: Repository Scaffolding, `.gitignore` & Environment Templates**
  - Create directory tree (`frontend/public-website`, `frontend/admin-dashboard`, `frontend/shared-theme`, `backend/spring-boot-app`, `ai-service`, `database`, `documentation`).
  - Write root `.gitignore`, `frontend/.env.example`, `backend/.env.example`, `ai-service/.env.example`.
- [ ] **Step 3: Single-Place Spiritual Theme Package & UI Primitives**
  - Create centralized theme tokens (`tokens.css` + `theme.ts` + Tailwind preset) with the mixed light/dark spiritual palette, non-generic saffron-terracotta/sandalwood buttons, and large `18px+` bilingual typography (`Noto Serif`, `Inter`, `Noto Sans Devanagari`).
  - Configure `shadcn/ui` base components overridden exclusively via the central theme tokens (no blue/black/white/green buttons, no small text).
- [ ] **Step 4: PostgreSQL Database Schema & Flyway Foundation**
  - Initialize Spring Boot 3 / Java 21 project with Maven/Gradle, `common` module (unified `ApiResponse`, `GlobalExceptionHandler`, audit/base entity, security config, OpenAPI).
  - Author Flyway migrations `V1`–`V8` covering Identity, Sources/Editions/Rights, Passages/References/Translations, Teachings, Claims/Evidence/Comparisons, Videos, Review/Audit/Submissions, and AI/Ingestion/pgvector tables.

### Phase 1 — Admin, Security & Source Ingestion Core (Steps 5–7)
- [ ] **Step 5: Authentication, RBAC & Audit Foundation (`auth`, `users`, `admin`)**
  - Implement JWT access/refresh flow, `SUPER_ADMIN` seeding via safe env-driven bootstrap, role guards (`SUPER_ADMIN`, `USER`, future roles), rate limiting, and immutable `audit_logs`.
- [ ] **Step 6: Source, Edition, Rights & Passage Management (`sources`, `scriptures`, `translations`)**
  - CRUD + status transitions for Sources, Editions, Rights/Licenses, Authors, and source-specific Reference hierarchies (Vedas Mandal/Sukta/Mantra, Quran Surah/Ayah, Bible Book/Chapter/Verse, Books Printed vs PDF page).
  - Separate storage for original text vs translations + translation provider/reviewer metadata.
- [ ] **Step 7: Document Upload, Extraction & Human Review Queue (`review`, `notifications`)**
  - Signed Cloudinary upload / safe document validation (MIME, size, SSRF checks), database-backed `ingestion_jobs` & `processing_jobs`.
  - Admin review workflow (`NEEDS_REVIEW` → `VERIFIED` → `PUBLISHED` / `NEEDS_CORRECTION`) with side-by-side source inspection and version snapshots (`content_versions`).

### Phase 2 — Public Knowledge Library & Bilingual UI (Steps 8–10)
- [ ] **Step 8: Public Search & Read APIs (`search`, public endpoints)**
  - PostgreSQL bilingual full-text search (English + Hindi + romanized transliteration support), exact reference lookup, and facets (source type, language, author, topic, verification status) returning strictly `PUBLISHED` records.
- [ ] **Step 9: Public Website Reference Screens (Home, Search, Scripture Reader)**
  - Build `frontend/public-website` mobile-first (`320px–430px` up to desktop):
    - Editorial Homepage (deep umber-rosewood masthead + warm parchment discovery flow).
    - Universal Search & Filter page (full-width mobile search, readable card results).
    - Scripture / Source Detail Reader (large `20px` passage reading view, printed vs PDF page badge, original + translation tabs, expandable citation & verification provenance).
  - Bilingual EN/HI switcher using maintained i18n resource files (`en.json`, `hi.json`).
- [ ] **Step 10: Public Trust, Methodology & Contribution Flows (`contributions`)**
  - About & Methodology, Copyright/Corrections/Privacy/Terms pages.
  - Rate-limited public submission forms (questions, source suggestions, corrections, claims, copyright complaints) + optional user bookmarks & saved collections.

### Phase 3 — Python AI Service & Citation-Validated RAG (Steps 11–13)
- [ ] **Step 11: FastAPI AI Service & Provider Adapter (`ai-service/`)**
  - Build FastAPI app with internal `AI_SERVICE_API_KEY` middleware, provider adapter interface (`GeminiAdapter`, `OpenAIAdapter`, `LocalAdapter`), and PyMuPDF/Tika + multilingual OCR pipeline for Hindi/English PDFs.
- [ ] **Step 12: Hybrid Indexing, Retrieval & Strict Citation Validation**
  - Chunk approved (`PUBLISHED`) passages with surrounding context, store embeddings in `embedding_records` (`pgvector`), run hybrid exact-ref + FTS + vector retrieval + reranking.
  - Enforce evidence-ID-only citations: Spring backend validates every returned evidence ID against PostgreSQL and constructs real public links + uncertainty disclosure.
- [ ] **Step 13: "Ask AI" Public Experience & Admin AI Controls**
  - Build public **Ask AI** page showing AI explanation cleanly separated from cited source cards, verification badges, and "insufficient evidence" notices.
  - Build Admin AI Settings, RAG indexing monitor, evaluation test-set runner, and provider-disable kill switch.

### Phase 4 — Research, Teachings, Claims & Comparisons (Steps 14–15)
- [ ] **Step 14: Teachings Library & Claim/Evidence Engine (`teachings`, `evidence`)**
  - Implement attributed teachings (speaker, exact quote vs paraphrase guard, context before/after, confidence & review history) and claims linked to Supporting / Contradicting / Contextual / Neutral / Inconclusive evidence.
- [ ] **Step 15: Comparative Research Views (`research`)**
  - Three-perspective side-by-side/stacked comparator: (1) Original Scripture, (2) Other Saints/Translators/Commentators, (3) Teachings of Sant Rampal Ji Maharaj — explicitly categorizing Agreement, Translation Difference, Interpretation Difference, Contradiction, Missing Context, and Inconclusive.
  - Build Admin management screens + Public Comparative Research & Claim Analysis pages.

### Phase 5 — Video Library & Timestamped Evidence (Step 16)
- [ ] **Step 16: Video Library & Verified Timestamped Segments (`videos`)**
  - Store third-party official video URLs/embeds (never unauthorized rehosting), integer-second `start_seconds`/`end_seconds` segments, links to scriptures/claims/topics, and fallback metadata view for non-timestamp platforms.
  - Build Public Video Library + Segment Detail + Admin Timestamp Editor.

### Phase 6 — Admin Polish & Production Readiness (Steps 17–18)
- [ ] **Step 17: Complete Admin Dashboard (`frontend/admin-dashboard`)**
  - Connect all 13 Admin views (Overview, Sources, Ingestion, Passage/Translation Review, Teachings, Claims/Evidence, Comparative Research, Videos, Public Submissions, AI Settings, RAG Evaluation, Copyright/Correction Reports, Audit Log) using the unified Spiritual Theme.
- [ ] **Step 18: End-to-End Verification, Accessibility & Security Hardening**
  - Automated unit & integration tests (Spring Boot + FastAPI + React), zero-overflow responsive audit (`320px`, `360px`, `390px`, `412px`, tablet, desktop), Hindi Devanagari legibility audit, keyboard/screen-reader audit, secret scan, rate-limit & backup/restore verification.

---

## 4. Session Handoff Log
- **Current Status:** Phase 0 — Step 1 completed (master plan + complete architectural, DB, API, editorial, ops, and spiritual UI/UX `.md` specs created).
- **Next Action When Ready:** Execute **Step 2** (create folder structure, `.gitignore`, `.env.example` templates) and **Step 3** (build the centralized single-place Spiritual Theme package).

