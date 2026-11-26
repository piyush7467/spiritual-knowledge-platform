# System Architecture

## 1. High-Level Topology

```text
[React + TS + Vite Frontend]
   ├── Public Website (Vercel)
   └── Admin Dashboard (Vercel / Protected Route)
            │ HTTPS + REST JSON + JWT
            ▼
[Spring Boot 3 Modular Monolith (Java 21)]
   ├── Public Read & Search APIs
   ├── Auth, RBAC, Rate Limiting & Audit
   ├── Editorial Review & Publishing Engine
   ├── Source / Scripture / Teaching / Claim / Video Modules
   └── AI Orchestration Client (validates all citations)
            │                                  │
            │ JPA / Flyway / JDBC              │ Internal HTTPS + AI_SERVICE_API_KEY
            ▼                                  ▼
[Neon PostgreSQL + pgvector + FTS]    [Python FastAPI AI Service]
                                         ├── Provider Adapters (Gemini / OpenAI / Local)
                                         ├── Document Extraction (PyMuPDF / Tika) & OCR
                                         ├── Chunking & Embedding Generator
                                         └── Hybrid Retrieval & Reranker
```

Media & authorized visual assets are stored in **Cloudinary**, using short-lived upload signatures generated exclusively by the Spring Boot backend.

---

## 2. Spring Boot Modular Monolith (`backend/spring-boot-app`)

Package root: `com.platform`

| Module | Responsibility |
|---|---|
| `common` | `ApiResponse<T>`, `GlobalExceptionHandler`, security filters, CORS, OpenAPI config, base audit entity, rate limiter |
| `auth` | Login, registration (optional), JWT access/refresh token rotation, password hashing |
| `users` | User accounts, roles (`SUPER_ADMIN`, `ADMIN`, `REVIEWER`, `CONTRIBUTOR`, `USER`), bookmarks, saved collections |
| `sources` | Sources, editions, source files, rights/license metadata, author attribution links |
| `scriptures` | Source-specific reference hierarchies, original passages, printed vs PDF page mapping |
| `translations` | Separate translations, provider/model tracking, human review status, domain glossary |
| `teachings` | Attributed statements by teachers/saints, exact quote vs paraphrase guard, surrounding context |
| `evidence` | Claims, evidence records (`SUPPORTING`, `CONTRADICTING`, `CONTEXTUAL`, `NEUTRAL`, `INCONCLUSIVE`) |
| `research` | Multi-perspective comparisons linking scriptures, commentators, and Sant Rampal Ji Maharaj's teachings |
| `videos` | Permitted third-party video URLs/embeds, integer-second `video_segments`, reference links |
| `search` | Unified keyword, exact reference, bilingual FTS, and faceted search across `PUBLISHED` content |
| `contributions` | Anonymous & user-submitted questions, source URLs, corrections, claims, copyright reports |
| `review` | Review queues, state transitions, verification rationale, `content_versions` snapshots |
| `notifications` | Job alerts, admin queue notifications, submission acknowledgments |
| `admin` | Super Admin overview, AI provider config, RAG index status, audit log viewer, monitoring |

### Architectural Boundary Rule
- Each module keeps internal `controller/`, `service/`, `repository/`, `entity/`, `dto/`, `mapper/`, and `exception/`.
- **Cross-module calls MUST go through public `*Service` / `*QueryPort` interfaces.** Never inject another module's JPA repository directly.

---

## 3. FastAPI AI Service (`ai-service/`)

- **Stateless Worker & RAG Engine:** Does **not** own authoritative source truth. Reads only `PUBLISHED` passage snapshots/embeddings (or receives text from Spring Boot jobs) and writes only processing outputs/embeddings scoped to AI tables.
- **Not Publicly Exposed:** Protected by `X-AI-Service-Key` (`AI_SERVICE_API_KEY`) and network restriction so only the Spring Boot backend calls it.
- **Provider Adapter Contract (`app/providers/base.py`):**
  - `generate_answer(prompt, context_chunks, language)`
  - `embed_texts(texts)`
  - `translate_draft(text, source_lang, target_lang)`
  - Concrete adapters: `GeminiAdapter`, `OpenAIAdapter`, `LocalAdapter` + kill-switch flag (`AI_ENABLED=true|false`).
- **Citation Guardrail Flow:**
  1. FastAPI returns answer text referencing chunk/evidence UUIDs (`[E:<uuid>]`) plus confidence/insufficiency flag.
  2. Spring Boot parses every `[E:<uuid>]`, verifies each exists in PostgreSQL with status `PUBLISHED`, strips or rejects any unverified ID, and hydrates real edition/page/URL metadata from the database before returning the response to the frontend.

