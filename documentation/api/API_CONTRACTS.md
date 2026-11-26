# API Contracts (`/api/v1`)

All public APIs use `publicId` (UUID), never internal database `id`.

---

## 1. Standard Response Envelope

```json
{
  "success": true,
  "data": {},
  "meta": {
    "page": 0,
    "size": 20,
    "totalElements": 142,
    "totalPages": 8,
    "requestId": "req_9f32a1"
  },
  "timestamp": "2026-11-26T17:20:00Z"
}
```

Errors return standard RFC-style structured payload:
```json
{
  "success": false,
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Requested passage was not found or is not published.",
    "fieldErrors": []
  },
  "timestamp": "2026-11-26T17:20:00Z"
}
```

---

## 2. Public Endpoints (No Login Required; Strictly `PUBLISHED` Data)

- `GET /api/v1/public/home` — Curated homepage sections, featured verified scripture & active topics.
- `GET /api/v1/public/search?q=&lang=&sourceType=&author=&topic=&ref=&page=&size=` — Bilingual FTS + exact reference + filtered search.
- `GET /api/v1/public/sources` & `GET /api/v1/public/sources/{publicId}` — Source catalog & edition/rights metadata.
- `GET /api/v1/public/passages/{publicId}` — Original passage, surrounding context, printed/PDF pages, translations, and linked evidence.
- `GET /api/v1/public/teachings` & `GET /api/v1/public/teachings/{publicId}` — Attributed teacher statements with context & source refs.
- `GET /api/v1/public/claims` & `GET /api/v1/public/claims/{publicId}` — Examined claims with supporting/contradicting/contextual evidence.
- `GET /api/v1/public/comparisons` & `GET /api/v1/public/comparisons/{publicId}` — 3-perspective comparative research views.
- `GET /api/v1/public/videos` & `GET /api/v1/public/videos/{publicId}/segments/{segmentPublicId}` — Permitted video links & verified integer-second timestamps.
- `POST /api/v1/public/ask` — Rate-limited Evidence-Grounded AI Q&A (returns AI explanation + backend-validated DB citations + uncertainty flag).
- `POST /api/v1/public/submissions` — Rate-limited public question, source suggestion, correction, claim, or copyright report.

---

## 3. Authenticated User & Admin Endpoints

### Auth & Bookmarks (`/api/v1/auth`, `/api/v1/me`)
- `POST /api/v1/auth/register`, `POST /api/v1/auth/login`, `POST /api/v1/auth/refresh`, `POST /api/v1/auth/logout`
- `GET /api/v1/me/collections`, `POST /api/v1/me/bookmarks`

### Admin Endpoints (`/api/v1/admin`, `SUPER_ADMIN` / `REVIEWER` RBAC + Audit Logged)
- `GET /api/v1/admin/overview` — Queue counts, indexing status, AI usage stats.
- `POST /api/v1/admin/sources`, `PUT /api/v1/admin/sources/{publicId}`, `POST /api/v1/admin/sources/{publicId}/upload-signature`
- `POST /api/v1/admin/ingestion/jobs`, `GET /api/v1/admin/ingestion/jobs/{publicId}`
- `GET /api/v1/admin/review/queue`, `POST /api/v1/admin/review/{entityType}/{publicId}/decision` (`VERIFIED`, `PUBLISHED`, `NEEDS_CORRECTION`, `REJECTED`)
- `CRUD /api/v1/admin/teachings`, `/api/v1/admin/claims`, `/api/v1/admin/evidence`, `/api/v1/admin/comparisons`, `/api/v1/admin/videos`
- `GET /api/v1/admin/submissions`, `PATCH /api/v1/admin/submissions/{publicId}`
- `GET /api/v1/admin/ai/settings`, `PUT /api/v1/admin/ai/settings`, `POST /api/v1/admin/ai/reindex`
- `GET /api/v1/admin/audit-logs`

---

## 4. Internal Spring ↔ FastAPI Contract (`/internal/v1`)

Protected by `X-AI-Service-Key` header (never exposed publicly):
- `POST /internal/v1/extract` — Extract text + OCR with confidence score & page-level boundaries.
- `POST /internal/v1/index/passages` — Chunk approved passages with surrounding context and store embeddings.
- `POST /internal/v1/rag/answer` — Hybrid retrieve + rerank + generate draft answer returning `[E:<passageOrEvidenceUuid>]` markers + `insufficientEvidence` boolean.
- `POST /internal/v1/translate/draft` — Produce labeled machine translation draft for human review.
- `GET /internal/v1/health` — Provider & index readiness check.

