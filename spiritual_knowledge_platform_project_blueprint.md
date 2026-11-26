# Spiritual Knowledge & Evidence Platform

## Product Requirements, Architecture, UI/UX and Environment Configuration

**Document status:** Initial project baseline\
**Architecture:** Module-based Spring Boot application + separate Python
AI service\
**Frontend:** React + TypeScript + Vite\
**Database:** Neon PostgreSQL\
**Media:** Cloudinary\
**Initial AI strategy:** Free-tier provider or locally runnable model,
behind a provider adapter\
**Languages:** Hindi and English\
**Design approach:** Mobile-first, single neutral theme

------------------------------------------------------------------------

# 1. Product Vision

Build a public spiritual knowledge and evidence platform centered on the
teachings of Sant Rampal Ji Maharaj and relevant original scriptures,
published translations, saints, spiritual teachers, commentators, and
other traceable sources.

The platform should help visitors:

-   Search and explore spiritual texts and attributed teachings.
-   Ask questions and receive answers supported by traceable sources.
-   Compare original scripture passages, translations, and attributed
    interpretations.
-   Examine claims from public websites and social media.
-   Explore videos and manually verified timestamped segments.
-   Open original references and inspect the surrounding context.
-   Submit questions, source suggestions, corrections, and claims for
    review.

Public reading and search should work without registration. Accounts can
be optional for bookmarks, saved collections, and contribution tracking.

## Core principle

**Every important published claim, quotation, comparison, and AI answer
must be traceable to source evidence.**

The platform must distinguish clearly between:

1.  Original source text.
2.  Published translation.
3.  Translator or commentator interpretation.
4.  Statement attributed to a specific teacher or author.
5.  Editorial research summary.
6.  AI-generated explanation.

The AI must not invent quotations, page numbers, verse references, URLs,
timestamps, or source attributions.

------------------------------------------------------------------------

# 2. Product Principles and Editorial Standards

-   Do not treat an interpretation as a direct quotation from scripture.
-   Do not misattribute a statement to a teacher or author.
-   Do not automatically label a different interpretation as false.
-   Show agreements, differences, missing context, translation
    differences, contradictions, and inconclusive findings separately.
-   A contradiction should only be reported when the claims are
    genuinely incompatible in context.
-   If evidence is insufficient, explicitly say so.
-   Preserve original text and source metadata.
-   Human review is required before important content is marked verified
    or published.
-   AI may suggest metadata and relationships, but cannot independently
    approve source evidence.
-   Keep a full audit trail and version history.
-   Publicly display source attribution and verification status.

## Evidence status vocabulary

Suggested statuses:

-   `DISCOVERED`: source URL or record found, not yet inspected.
-   `REGISTERED`: source metadata entered.
-   `PROCESSING`: extraction or OCR in progress.
-   `NEEDS_REVIEW`: extracted content awaiting human review.
-   `VERIFIED`: evidence checked against the source.
-   `PUBLISHED`: approved for public use.
-   `REJECTED`: not suitable or not supported.
-   `NEEDS_CORRECTION`: an issue was identified.
-   `ARCHIVED`: no longer active but retained for history.

Do not use `VERIFIED` as a synonym for "the religious conclusion is
true." It means the record and its attribution/reference have been
checked against the stated source.

------------------------------------------------------------------------

# 3. Technology Stack

## Frontend

-   React
-   TypeScript
-   Vite
-   Tailwind CSS
-   shadcn/ui as the main component foundation
-   Lucide React icons
-   React Router
-   TanStack Query
-   React Hook Form
-   Zod

## Main backend

-   Java 21
-   Spring Boot
-   Spring Security
-   Spring Data JPA / Hibernate
-   Bean Validation
-   Flyway database migrations
-   Springdoc OpenAPI

The main backend is a **modular monolith**. Modules have clear
boundaries and communicate through service interfaces. Avoid direct
cross-module repository access.

## AI backend

-   Python
-   FastAPI
-   Provider adapter layer
-   Retrieval-Augmented Generation (RAG)
-   Document extraction and OCR
-   Embedding generation
-   Retrieval and reranking
-   Citation validation
-   Research and claim-analysis workflows

The AI service is a separate deployment unit. It does not own the
authoritative source records; the main backend/database remains the
system of record.

## Data and storage

-   Neon PostgreSQL
-   pgvector, subject to the selected Neon plan and extension
    availability
-   PostgreSQL full-text search
-   Cloudinary for images and authorized visual assets
-   Original third-party video URLs and permitted official embeds

## Initial deployment shape

1.  React frontend on Vercel.
2.  One Spring Boot backend application.
3.  One Python FastAPI AI service.
4.  Neon PostgreSQL.
5.  Cloudinary.

Do not begin with Eureka, Config Server, a separate API Gateway,
Kubernetes, or many independently deployed microservices. Extract
services only when scale, team size, or operational needs justify it.

------------------------------------------------------------------------

# 4. Repository Structure

``` text
spiritual-knowledge-platform/
├── frontend/
│   ├── public-website/
│   └── admin-dashboard/
│
├── backend/
│   └── spring-boot-app/
│       └── src/main/java/com/platform/
│           ├── common/
│           │   ├── config/
│           │   ├── security/
│           │   ├── exception/
│           │   ├── response/
│           │   └── audit/
│           ├── auth/
│           ├── users/
│           ├── sources/
│           ├── scriptures/
│           ├── translations/
│           ├── teachings/
│           ├── evidence/
│           ├── research/
│           ├── videos/
│           ├── search/
│           ├── contributions/
│           ├── review/
│           ├── notifications/
│           └── admin/
│
├── ai-service/
│   └── app/
│       ├── api/
│       ├── providers/
│       ├── rag/
│       ├── retrieval/
│       ├── ingestion/
│       ├── extraction/
│       ├── ocr/
│       ├── embeddings/
│       ├── translation/
│       ├── research/
│       ├── evaluation/
│       └── core/
│
├── database/
│   ├── migrations/
│   └── seed/
│
├── documentation/
│   ├── requirements/
│   ├── architecture/
│   ├── database/
│   ├── api/
│   ├── ui-ux/
│   └── operations/
│
├── .gitignore
└── README.md
```

Each Spring module should generally contain:

``` text
module/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
└── exception/
```

Keep module boundaries explicit. For example, the Research module should
call a public service interface from the Scripture module rather than
querying Scripture repositories directly.

------------------------------------------------------------------------

# 5. Functional Modules

## 5.1 Authentication and users

-   Public visitors can browse without login.
-   Optional registration and login.
-   Role-based access control.
-   Initial roles: `SUPER_ADMIN`, `ADMIN` (future), `REVIEWER` (future),
    `CONTRIBUTOR` (future), `USER`.
-   Super Admin controls publishing and verification.
-   Users may save bookmarks and collections.
-   Rate-limit anonymous and authenticated submissions.

## 5.2 Source management

Manage books, scriptures, editions, websites, articles, videos, and
other source types.

Store:

-   Title and alternate titles.
-   Source type.
-   Author, attributed author, or organization.
-   Publisher.
-   Edition.
-   Publication year.
-   Translator.
-   Language.
-   ISBN or other identifier when available.
-   Source URL.
-   Original publisher or hosting organization.
-   Licence/copyright status.
-   Permission details.
-   Attribution requirements.
-   Access restrictions.
-   Verification status.
-   Created/updated timestamps.
-   Version history.

## 5.3 Scripture library

Support source-specific reference structures rather than assuming every
scripture uses chapter and verse.

Examples:

-   Vedas: Mandal, Sukta, Mantra.
-   Quran: Surah and Ayah.
-   Bible: Book, Chapter, Verse.
-   Other texts: source-specific hierarchy.
-   Books: edition, printed page, section, paragraph.
-   Scanned documents: PDF page stored separately from printed page.

Store original passage text separately from translations and
commentaries.

## 5.4 Teachings library

Store statements attributed to identifiable teachers, saints, authors,
and commentators.

Each record should include:

-   Person or organization.
-   Exact attributed text, if available.
-   Source book, discourse, transcript, or video.
-   Edition and printed page, or timestamp.
-   Original language.
-   Translation information.
-   Context before and after the passage.
-   Attribution confidence and verification status.
-   Reviewer and review history.

Never present a paraphrase as a direct quotation.

## 5.5 Evidence and claim management

A claim is an individual statement that can be examined against sources.

Evidence records can be:

-   Supporting.
-   Contradicting.
-   Contextual.
-   Neutral/background.
-   Inconclusive.

Store the exact passage or excerpt, source reference, context, evidence
relationship, reviewer, status, and rationale.

## 5.6 Comparative research

Present distinct perspectives independently:

1.  Original scripture/source passage.
2.  Other saints, teachers, translators, or commentators.
3.  Teachings attributed to Sant Rampal Ji Maharaj.

Then show:

-   Areas of agreement.
-   Translation differences.
-   Interpretation differences.
-   Contradictory claims, if genuinely supported.
-   Missing context.
-   Inconclusive evidence.

Every perspective and finding must link to its supporting evidence.

## 5.7 Video library

-   Store original third-party video URLs.
-   Use official embeds or links only where permitted.
-   Admin manually adds and verifies timestamped segments.
-   Store start and end time as integer seconds.
-   Link segments to topics, claims, and scripture references.
-   Do not download or rehost third-party videos without authorization.
-   Some platforms do not support timestamped playback; in such cases,
    show the time range as editorial metadata and link to the original
    video.

Suggested video tables:

-   `videos`
-   `video_segments`
-   `video_segment_references`

## 5.8 Search

Support:

-   Keyword search.
-   Exact source-reference search.
-   Full-text search.
-   Semantic/vector search.
-   Hindi and English.
-   Romanized Hindi where feasible.
-   Filters for source type, author, language, edition, topic, and
    verification status.

## 5.9 Public contributions

Allow visitors to submit without registration:

-   Questions.
-   Source URLs.
-   Corrections.
-   Claims for examination.
-   Feedback and copyright complaints.

Submissions must be rate-limited, spam-protected, private until
reviewed, and never automatically published.

## 5.10 Administration

Centralized Super Admin dashboard:

-   Source management.
-   Document ingestion.
-   Scripture and passage review.
-   Translation review.
-   Teaching attribution review.
-   Evidence and claim review.
-   Comparative research management.
-   Video and segment management.
-   AI provider settings.
-   RAG indexing status.
-   User submissions.
-   Copyright and correction reports.
-   Audit logs.
-   Usage and error monitoring.

------------------------------------------------------------------------

# 6. Database Design Baseline

The following is a logical starting point. Final SQL types, constraints,
indexes, foreign keys, and migration order must be designed before
implementation.

## Identity

-   `users`
-   `roles`
-   `permissions`
-   `user_roles`
-   `role_permissions`
-   `user_bookmarks`
-   `user_collections`

## Sources and editions

-   `sources`
-   `source_editions`
-   `source_files`
-   `source_rights`
-   `source_authors`
-   `source_author_links`

## Passages and translations

-   `source_passages`
-   `source_references`
-   `source_translations`
-   `passage_relationships`

## Teachings

-   `teachers`
-   `teachings`
-   `teaching_references`

## Topics, claims, and evidence

-   `topics`
-   `claims`
-   `evidence_records`
-   `claim_evidence`
-   `comparisons`
-   `comparison_items`

## Videos

-   `videos`
-   `video_segments`
-   `video_segment_references`

## Review and audit

-   `review_tasks`
-   `verification_reviews`
-   `content_versions`
-   `audit_logs`
-   `public_submissions`
-   `copyright_reports`
-   `correction_reports`

## AI and processing

-   `ingestion_jobs`
-   `processing_jobs`
-   `embedding_records`
-   `ai_requests`
-   `retrieval_logs`
-   `ai_evaluations`
-   `provider_usage_logs`

## Important database rules

1.  Use internal database IDs and separate public reference IDs.
2.  Use UUIDs or another non-sequential public identifier where
    appropriate.
3.  Keep printed page and PDF page separate.
4.  Keep original text and translated text separate.
5.  Store source edition and language with each evidence reference.
6.  Store timestamps as integer seconds.
7.  Use explicit status fields and audit timestamps.
8.  Retain old content versions.
9.  Add indexes for source references, language, status, topic, and
    search.
10. Use database constraints to protect relationships and prevent
    invalid publication states.
11. Do not store secrets, API keys, or database passwords in database
    content.
12. Use Flyway migrations; do not rely on automatic schema mutation in
    production.

------------------------------------------------------------------------

# 7. Document Ingestion and Verification Workflow

Example workflow for an authorized PDF:

1.  Admin registers source metadata and rights information.
2.  Admin uploads the document.
3.  System checks file type, size, and safety.
4.  Extract text using PyMuPDF or Apache Tika.
5.  Use OCR for scanned pages, with multilingual support as required.
6.  Preserve PDF page and printed page separately.
7.  Detect language and source structure.
8.  Separate original text, published translations, commentaries, and
    attributed teachings.
9.  AI suggests metadata, topics, and possible cross-references.
10. Admin checks the extracted text against the source.
11. Approved passages are chunked with surrounding context.
12. Generate embeddings.
13. Index approved records in PostgreSQL full-text search and pgvector.
14. Publish only approved records.

The system should retain extraction confidence and flag uncertain OCR
text. It must not silently correct source text.

------------------------------------------------------------------------

# 8. AI and RAG Architecture

Use Retrieval-Augmented Generation rather than training a large language
model from scratch.

## Provider abstraction

Create a provider interface so the application is not locked to one
provider.

Potential adapters:

-   OpenAI.
-   Gemini.
-   Other providers added later.
-   Local/open-source model adapter where hardware and licence permit.

Start with one provider that has an available free tier or use a local
model for development. Free quotas, commercial-use conditions, model
availability, and API pricing can change. Do not assume that a free chat
website includes free API access.

## RAG workflow

1.  Detect user language.
2.  Normalize the question.
3.  Identify named sources, references, and topics.
4.  Search exact references.
5.  Search PostgreSQL full-text index.
6.  Search vector embeddings.
7.  Apply metadata and verification filters.
8.  Rerank results where appropriate.
9.  Retrieve the original passage and sufficient surrounding context.
10. Send only relevant evidence to the model.
11. Generate an answer with evidence IDs.
12. Backend validates evidence IDs against database records.
13. Backend constructs source links and citation metadata.
14. Return answer with citations and uncertainty disclosure.

The LLM must not construct authoritative citation URLs or invent
database IDs. The backend resolves evidence IDs to actual source
records.

## AI safety and quality

-   Only approved records are eligible for production evidence
    retrieval.
-   Separate internal approved evidence from external web discovery.
-   Label external information distinctly.
-   Do not let AI modify verified source records.
-   Log retrieval evidence and provider usage, subject to privacy and
    retention rules.
-   Use rate limits, quotas, caching, and a provider-disable switch.
-   Evaluate retrieval precision, citation correctness,
    unsupported-claim rate, answer relevance, and Hindi/English quality.
-   Build a test dataset of questions with expected sources and
    acceptable evidence.

------------------------------------------------------------------------

# 9. Translation Strategy

Initial languages: Hindi and English.

Use a translation adapter rather than tightly coupling application code
to one vendor.

Google Cloud Translation may be evaluated for automatic translation. Its
API quotas, billing requirements, free usage, and current pricing must
be checked before production use. A consumer-facing free translation
page is not equivalent to a free API.

## Translation rules

-   Preserve original text unchanged.
-   Store translated text separately.
-   Record provider and model/version.
-   Record target language.
-   Mark machine-generated translations clearly.
-   Human-review important scripture quotations and religious
    terminology.
-   Maintain a glossary for recurring names and domain-specific terms.
-   Never silently replace a published translation with a new machine
    translation.
-   Cache translation results to reduce repeated requests.
-   UI labels should use maintained translation resource files, not
    machine translation on every page load.

Suggested translation fields:

-   `original_passage_id`
-   `target_language`
-   `translated_text`
-   `translation_provider`
-   `translation_model`
-   `translation_status`
-   `reviewed_by`
-   `reviewed_at`

------------------------------------------------------------------------

# 10. UI/UX Design System

## Design direction

Use a **premium editorial knowledge-library aesthetic**.

The site should feel trustworthy, calm, readable, modern, and
professional. Avoid generic AI landing-page styling.

Use one neutral theme initially. Do not build a light/dark mode switch
in the first version.

Suggested starting tokens:

  Token              Value
  ------------------ -------------------
  Background         `#F8F7F3`
  Main text          `#252820`
  Primary accent     `#526B57`
  Secondary accent   `#EAE4D8`
  Surface            `#FFFFFF`
  Border             Soft neutral grey

Typography:

-   Noto Serif for selected editorial headings.
-   Inter for English body text.
-   Noto Sans Devanagari for Hindi.

Validate font loading, line height, and Devanagari rendering on real
mobile screens.

## Component strategy

-   shadcn/ui: primary component foundation.
-   Lucide React: consistent icons.
-   Aceternity UI and Magic UI: use selected components only when they
    genuinely improve the experience.
-   Avoid excessive gradients, glassmorphism, motion, and decorative
    effects.
-   Avoid using the same generic card grid for every section.

## Mobile-first requirements

Design for 320px--430px screens first, then tablet and desktop.

-   No horizontal overflow on standard pages.
-   Minimum 44--48px touch targets.
-   Responsive typography and spacing.
-   Compact accessible navigation.
-   Full-width mobile search.
-   Stack comparison sections vertically on mobile.
-   Transform wide data tables into readable cards where appropriate.
-   Use expandable evidence/source details.
-   Keep forms single-column on small screens.
-   Lazy-load images and video embeds.
-   Optimize image sizes and loading.
-   Make Hindi text comfortable to read.
-   Test 320px, 360px, 390px, 412px, tablet, laptop, and large desktop
    widths.

## Main public pages

1.  Home page.
2.  Universal search.
3.  Search results and filters.
4.  Scripture library.
5.  Scripture/source detail.
6.  Teaching detail.
7.  Comparative research.
8.  Ask AI.
9.  Claim analysis.
10. Video library.
11. Video segment detail.
12. Evidence detail.
13. About and methodology.
14. Privacy, terms, copyright, and correction pages.
15. Public submission forms.

## Admin pages

1.  Admin overview.
2.  Source management.
3.  Document upload and processing.
4.  Passage and translation review.
5.  Teaching management.
6.  Claims and evidence.
7.  Comparative research.
8.  Video and timestamp management.
9.  Public submissions.
10. AI provider and usage configuration.
11. RAG indexing and evaluation.
12. Reports and moderation.
13. Audit log.

------------------------------------------------------------------------

# 11. UI/UX Skills and Design Resources

Evaluate these resources for Antigravity or compatible coding agents.
Check each repository's current instructions, licence, supported agent
format, and installation steps before use.

## Design skills

-   Antigravity Design Skills: https://github.com/
-   UI UX Pro Max:
    https://github.com/nextlevelbuilder/ui-ux-pro-max-skill
-   AgentUI Skills: https://github.com/VelorioLabs/AgentUI-Skills

Repository names and installation instructions can change. Verify the
current official repository before installing.

## Component libraries

-   shadcn/ui: https://ui.shadcn.com/
-   Aceternity UI: https://ui.aceternity.com/
-   Magic UI: https://magicui.design/
-   Lucide: https://lucide.dev/

Use shadcn/ui as the foundation. Use animated component libraries
selectively.

## Suggested Antigravity workflow

1.  Inspect the existing project before changing files.
2.  Establish design tokens and shared components first.
3.  Build the homepage and scripture detail page as reference pages.
4.  Review screenshots at mobile, tablet, and desktop widths.
5.  Fix visual hierarchy, spacing, typography, and overflow.
6.  Reuse the approved design system on other pages.
7.  Preserve API contracts and business logic when redesigning existing
    screens.
8.  Implement loading, error, empty, and success states.
9.  Verify keyboard accessibility and semantic HTML.

------------------------------------------------------------------------

# 12. Security, Privacy, and Secret Management

## Non-negotiable secret rule

**All private credentials, API keys, passwords, signing keys, and tokens
must be stored in environment variables or a deployment secret manager.
Never hardcode secrets in source code, prompts, frontend bundles,
documentation, database seed data, screenshots, or Git commits.**

## Environment files

Use separate environment configuration for each application:

``` text
project/
├── frontend/
│   └── .env.example
├── backend/
│   └── .env.example
├── ai-service/
│   └── .env.example
└── .gitignore
```

Actual `.env` files stay local and must be ignored by Git.

Commit `.env.example` files containing variable names and safe
placeholders only. Never put real credentials in example files.

## Root `.gitignore`

At minimum:

``` gitignore
# Environment and secrets
.env
.env.*
!.env.example
!.env.*.example

# Java
**/target/
*.class

# Python
**/__pycache__/
.venv/
venv/
.pytest_cache/
*.pyc

# Node
node_modules/
dist/
.vite/

# IDE and OS
.idea/
.vscode/
.DS_Store
Thumbs.db

# Logs and local data
*.log
logs/
tmp/
uploads/
```

Review ignore rules carefully so that safe example files remain
committed and real secrets are excluded.

## Frontend environment

Vite exposes variables prefixed with `VITE_` to browser code. Therefore:

-   Only put public configuration in `VITE_` variables.
-   Never put database passwords, Cloudinary API secrets, AI keys,
    signing keys, or private tokens in frontend variables.
-   Public API base URL is acceptable.
-   A Cloudinary cloud name may be public, but upload signing must
    happen on the backend.
-   Use backend-generated signed upload parameters where appropriate.

Example `frontend/.env.example`:

``` dotenv
VITE_APP_NAME=Spiritual Knowledge Platform
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_DEFAULT_LANGUAGE=en
```

## Spring Boot environment

Example `backend/.env.example`:

``` dotenv
SPRING_PROFILES_ACTIVE=local
SERVER_PORT=8080

# Neon PostgreSQL
DB_URL=jdbc:postgresql://HOST/DB_NAME?sslmode=require
DB_USERNAME=YOUR_DB_USERNAME
DB_PASSWORD=CHANGE_ME

# JWT - use a cryptographically secure secret/key in real environments
JWT_SECRET=CHANGE_ME_WITH_A_LONG_RANDOM_SECRET
JWT_ACCESS_TOKEN_TTL_SECONDS=900
JWT_REFRESH_TOKEN_TTL_SECONDS=604800

# Cloudinary
CLOUDINARY_CLOUD_NAME=YOUR_CLOUD_NAME
CLOUDINARY_API_KEY=YOUR_API_KEY
CLOUDINARY_API_SECRET=CHANGE_ME

# AI service connection
AI_SERVICE_BASE_URL=http://localhost:8000
AI_SERVICE_API_KEY=CHANGE_ME

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

Use Spring configuration placeholders to map environment variables. Do
not commit actual values.

For production, prefer a platform secret manager. Use separate
credentials for development, staging, and production. Restrict database
roles to the minimum permissions required.

## Python AI service environment

Example `ai-service/.env.example`:

``` dotenv
APP_ENV=local
APP_HOST=0.0.0.0
APP_PORT=8000

# Internal service authentication
AI_SERVICE_API_KEY=CHANGE_ME

# PostgreSQL - use only if the AI service needs direct DB access
DATABASE_URL=postgresql://USER:PASSWORD@HOST/DB_NAME?sslmode=require

# Provider selection
AI_PROVIDER=YOUR_SELECTED_PROVIDER

# Example provider key - configure only the provider actually used
OPENAI_API_KEY=
GEMINI_API_KEY=

# Embeddings
EMBEDDING_PROVIDER=YOUR_EMBEDDING_PROVIDER
EMBEDDING_MODEL=YOUR_EMBEDDING_MODEL

# Retrieval
VECTOR_TOP_K=8
FULL_TEXT_TOP_K=8

# Processing limits
MAX_UPLOAD_SIZE_MB=25
MAX_DOCUMENT_PAGES=500
```

Do not configure or expose unused provider keys. The AI service should
authenticate requests from the Spring backend and should not be an
unrestricted public endpoint.

## Environment management rules

1.  Keep `.env` files out of Git.
2.  Commit only `.env.example`.
3.  Never paste real secrets into AI prompts or public issue trackers.
4.  Use separate credentials per environment.
5.  Rotate credentials immediately if exposed.
6.  Restrict database and Cloudinary permissions.
7.  Never expose private API keys through browser JavaScript.
8.  Use HTTPS in production.
9.  Validate and limit file uploads.
10. Protect admin endpoints with authorization and audit logging.
11. Rate-limit AI and public submission endpoints.
12. Configure backups and test restoration.
13. Avoid logging passwords, tokens, or sensitive request payloads.
14. Use secret managers provided by hosting platforms for production
    deployment.

## Other security controls

-   Strong authentication and role-based authorization.
-   Object-level authorization to prevent IDOR.
-   Input validation and output encoding.
-   CSRF protection where cookie-based authentication is used.
-   Secure cookies and appropriate token rotation.
-   CORS allowlist.
-   SSRF protection for source URL ingestion.
-   File MIME/type and size validation.
-   Malware scanning where practical.
-   Safe HTML rendering; sanitize user-submitted rich text.
-   Audit logs for admin actions.
-   Privacy-aware retention policies.
-   Abuse protection and rate limits.

------------------------------------------------------------------------

# 13. Copyright and External Sources

Public availability does not automatically grant permission to download,
reproduce, translate, or republish content.

-   Record rights and licence metadata.
-   Use official links and permitted embeds.
-   Do not download or rehost third-party videos without authorization.
-   Do not scrape arbitrary websites or embed HTML.
-   Respect publisher, platform, API, and embedding terms.
-   Keep source attribution visible.
-   Provide correction and copyright complaint workflows.
-   Retain a record of takedown and correction decisions.
-   Have applicable Indian copyright and privacy obligations reviewed by
    a qualified professional before launch.

------------------------------------------------------------------------

# 14. Performance, Reliability, and Operations

Initial implementation should remain simple.

-   Use database-backed job records for ingestion and processing.
-   Start with scheduled/background jobs; introduce RabbitMQ only when
    required.
-   Use pagination for large lists.
-   Cache frequently accessed public source pages and search results
    where appropriate.
-   Lazy-load images and third-party embeds.
-   Use timeouts and retries for external provider calls.
-   Prevent duplicate ingestion jobs.
-   Record processing failures and allow safe retries.
-   Add health endpoints and structured logs.
-   Monitor database usage, AI provider usage, errors, and latency.
-   Maintain backup and restore procedures.
-   Use database migrations for every schema change.

------------------------------------------------------------------------

# 15. Development Roadmap

## Phase 0 --- Foundation

-   Confirm requirements and architecture.
-   Create repository structure.
-   Configure environment files and `.gitignore`.
-   Configure Neon PostgreSQL.
-   Set up Flyway.
-   Establish UI design tokens.
-   Configure local development and API documentation.

## Phase 1 --- Admin and source foundation

-   Authentication and Super Admin.
-   Source and edition management.
-   Rights and licence metadata.
-   Document upload.
-   Ingestion job tracking.
-   Passage extraction and review.
-   Audit history.

## Phase 2 --- Public knowledge library

-   Scripture and source pages.
-   Hindi/English interface.
-   Search and filters.
-   Source detail and evidence links.
-   Responsive public homepage.

## Phase 3 --- AI question answering

-   FastAPI service.
-   Provider abstraction.
-   Embeddings and vector retrieval.
-   Full-text retrieval.
-   Citation validation.
-   Answer evaluation and usage limits.

## Phase 4 --- Research features

-   Teachings library.
-   Claim and evidence management.
-   Comparative research.
-   Debate/research modes.
-   Public question and correction submissions.

## Phase 5 --- Media and advanced research

-   Video library.
-   Timestamped segments.
-   Social media claim analysis.
-   External source discovery.
-   Additional AI providers.
-   Voice search if justified.

## Phase 6 --- Production readiness

-   Security review.
-   Copyright and privacy review.
-   Accessibility review.
-   Mobile responsiveness testing.
-   Load and reliability testing.
-   Backup/restore test.
-   Monitoring and deployment documentation.

------------------------------------------------------------------------

# 16. Definition of Done

A feature is not complete merely because its main screen renders.

For each feature, verify:

-   Correct API integration.
-   Server-side validation and authorization.
-   Loading, error, empty, and success states.
-   Mobile, tablet, and desktop layouts.
-   Keyboard and screen-reader accessibility where applicable.
-   Hindi and English behavior.
-   No horizontal overflow.
-   No secrets in source code or browser bundles.
-   Unit and integration tests.
-   Audit and verification behavior where relevant.
-   Documentation and environment example updates.

For source/evidence features additionally verify:

-   Exact source edition.
-   Original text preservation.
-   Printed and PDF page distinction.
-   Correct reference hierarchy.
-   Attribution and rights metadata.
-   Reviewer and verification history.
-   Generated public link resolves to the correct record.

For AI features additionally verify:

-   Retrieved evidence is actually relevant.
-   Citations resolve to real database records.
-   No fabricated page numbers or quotations.
-   Insufficient evidence is disclosed.
-   Provider usage is limited and logged.
-   Unapproved content is excluded from production retrieval.

------------------------------------------------------------------------

# 17. Immediate Next Steps

Before implementing all features, complete these in order:

1.  Approve this product and architecture baseline.
2.  Finalize database ERD and migration plan.
3.  Define source, passage, evidence, and review state models.
4.  Define backend module contracts and API endpoints.
5.  Create environment templates and verify secret handling.
6.  Build the design system.
7.  Implement the public homepage and scripture detail page.
8.  Build source ingestion and admin review before production AI.

**Final direction:** Build a trustworthy, mobile-first evidence library
first. AI, comparison, and claim analysis should be built on top of
verified, well-structured source data---not used as substitutes for it.
