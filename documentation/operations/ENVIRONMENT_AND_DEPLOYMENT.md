# Environment Configuration, Security & Deployment Operations

---

## 1. Non-Negotiable Secret Hygiene

- **Zero secrets** in source code, `VITE_*` variables, SQL seeds, documentation, screenshots, or Git commits.
- Only `.env.example` templates with safe placeholders are committed; local `.env` files are Git-ignored.
- Separate credentials per environment (`local`, `staging`, `production`).

---

## 2. Environment Templates

### `frontend/.env.example` (Public Browser-Safe Only)
```dotenv
VITE_APP_NAME=Spiritual Knowledge Platform
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_DEFAULT_LANGUAGE=en
```

### `backend/.env.example` (Spring Boot)
```dotenv
SPRING_PROFILES_ACTIVE=local
SERVER_PORT=8080

DB_URL=jdbc:postgresql://HOST/DB_NAME?sslmode=require
DB_USERNAME=YOUR_DB_USERNAME
DB_PASSWORD=CHANGE_ME

JWT_SECRET=CHANGE_ME_WITH_A_LONG_RANDOM_SECRET
JWT_ACCESS_TOKEN_TTL_SECONDS=900
JWT_REFRESH_TOKEN_TTL_SECONDS=604800

CLOUDINARY_CLOUD_NAME=YOUR_CLOUD_NAME
CLOUDINARY_API_KEY=YOUR_API_KEY
CLOUDINARY_API_SECRET=CHANGE_ME

AI_SERVICE_BASE_URL=http://localhost:8000
AI_SERVICE_API_KEY=CHANGE_ME

CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:5174
```

### `ai-service/.env.example` (FastAPI)
```dotenv
APP_ENV=local
APP_HOST=0.0.0.0
APP_PORT=8000

AI_SERVICE_API_KEY=CHANGE_ME
AI_ENABLED=true
DATABASE_URL=postgresql://USER:PASSWORD@HOST/DB_NAME?sslmode=require

AI_PROVIDER=gemini
OPENAI_API_KEY=
GEMINI_API_KEY=

EMBEDDING_PROVIDER=gemini
EMBEDDING_MODEL=text-embedding-004

VECTOR_TOP_K=8
FULL_TEXT_TOP_K=8

MAX_UPLOAD_SIZE_MB=25
MAX_DOCUMENT_PAGES=500
```

---

## 3. Security Controls Checklist

- JWT access/refresh tokens + BCrypt/Argon2 password hashing + RBAC method security (`@PreAuthorize`).
- UUID `publicId` lookup everywhere to block IDOR.
- SSRF allowlist/IP-block validation before fetching external source URLs.
- Strict MIME/magic-byte & size validation on document uploads; signed Cloudinary parameters generated server-side.
- Sanitized HTML/Markdown rendering on all user-visible text.
- Rate limiting on `/api/v1/public/ask`, `/api/v1/public/submissions`, and `/api/v1/auth/*`.

---

## 4. Initial Production Deployment Shape

1. **Public Frontend & Admin Dashboard:** Vercel (separate deployments or route-split builds).
2. **Spring Boot Backend:** Single container/VM instance (no Eureka/K8s/microservices overhead initially).
3. **Python FastAPI AI Service:** Single private container reachable only from Spring Boot via `AI_SERVICE_API_KEY`.
4. **Database:** Neon PostgreSQL (`pgvector` + automated branching/backups).
5. **Media:** Cloudinary.

