-- V8: Ingestion Jobs, AI Requests, Retrieval Logs & Vector Chunks
-- Note: Enable pgvector extension on Neon when vector storage is active:
-- CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE ingestion_jobs (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    source_edition_id BIGINT NOT NULL REFERENCES source_editions(id) ON DELETE CASCADE,
    source_file_id BIGINT REFERENCES source_files(id) ON DELETE SET NULL,
    job_status VARCHAR(30) NOT NULL DEFAULT 'QUEUED',
    attempt_count INTEGER NOT NULL DEFAULT 0,
    error_message TEXT,
    requested_by BIGINT REFERENCES users(id),
    started_at TIMESTAMPTZ,
    completed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE processing_jobs (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    ingestion_job_id BIGINT REFERENCES ingestion_jobs(id) ON DELETE CASCADE,
    step_name VARCHAR(60) NOT NULL,
    step_status VARCHAR(30) NOT NULL DEFAULT 'QUEUED',
    metrics_json JSONB,
    error_message TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE embedding_records (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    passage_id BIGINT NOT NULL REFERENCES source_passages(id) ON DELETE CASCADE,
    chunk_index INTEGER NOT NULL,
    chunk_text TEXT NOT NULL,
    context_before TEXT,
    context_after TEXT,
    embedding_model VARCHAR(120) NOT NULL,
    embedding_json JSONB,
    is_published_eligible BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (passage_id, chunk_index, embedding_model)
);

CREATE TABLE ai_requests (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    user_id BIGINT REFERENCES users(id),
    language_code VARCHAR(16) NOT NULL,
    question_text TEXT NOT NULL,
    normalized_question TEXT,
    insufficient_evidence BOOLEAN NOT NULL DEFAULT FALSE,
    response_status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    latency_ms INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE retrieval_logs (
    id BIGSERIAL PRIMARY KEY,
    ai_request_id BIGINT NOT NULL REFERENCES ai_requests(id) ON DELETE CASCADE,
    passage_public_id UUID NOT NULL,
    retrieval_channel VARCHAR(30) NOT NULL,
    score NUMERIC(8,5),
    cited_in_answer BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE ai_evaluations (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    test_question TEXT NOT NULL,
    expected_passage_public_id UUID,
    citation_correct BOOLEAN,
    unsupported_claim_detected BOOLEAN,
    notes TEXT,
    evaluated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE provider_usage_logs (
    id BIGSERIAL PRIMARY KEY,
    provider_name VARCHAR(50) NOT NULL,
    model_name VARCHAR(120) NOT NULL,
    operation_type VARCHAR(40) NOT NULL,
    input_tokens INTEGER DEFAULT 0,
    output_tokens INTEGER DEFAULT 0,
    status_code INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_embedding_eligible ON embedding_records(is_published_eligible, passage_id);
CREATE INDEX idx_ingestion_jobs_status ON ingestion_jobs(job_status);

