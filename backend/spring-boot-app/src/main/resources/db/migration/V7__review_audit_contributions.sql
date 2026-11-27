-- V7: Review Tasks, Verification History, Content Versions, Audit Logs & Public Reports
CREATE TABLE review_tasks (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    entity_type VARCHAR(50) NOT NULL,
    entity_public_id UUID NOT NULL,
    assigned_to BIGINT REFERENCES users(id),
    priority INTEGER NOT NULL DEFAULT 3,
    task_status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE verification_reviews (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_public_id UUID NOT NULL,
    previous_status VARCHAR(30) NOT NULL,
    new_status VARCHAR(30) NOT NULL,
    reviewer_id BIGINT NOT NULL REFERENCES users(id),
    rationale TEXT NOT NULL,
    reviewed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE content_versions (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_public_id UUID NOT NULL,
    version_number INTEGER NOT NULL,
    snapshot_json JSONB NOT NULL,
    changed_by BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    actor_user_id BIGINT REFERENCES users(id),
    actor_role VARCHAR(40),
    action_code VARCHAR(80) NOT NULL,
    entity_type VARCHAR(50),
    entity_public_id UUID,
    details_json JSONB,
    ip_address VARCHAR(64),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE public_submissions (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    submission_type VARCHAR(40) NOT NULL,
    submitted_by_user_id BIGINT REFERENCES users(id),
    language_code VARCHAR(16) NOT NULL DEFAULT 'en',
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    reference_url TEXT,
    moderation_status VARCHAR(30) NOT NULL DEFAULT 'NEW',
    reviewed_by BIGINT REFERENCES users(id),
    reviewer_notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_submission_type CHECK (
        submission_type IN ('QUESTION', 'SOURCE_SUGGESTION', 'CORRECTION', 'CLAIM', 'FEEDBACK')
    )
);

CREATE TABLE copyright_reports (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    reporter_name VARCHAR(200) NOT NULL,
    reporter_email VARCHAR(255) NOT NULL,
    target_entity_type VARCHAR(50),
    target_public_id UUID,
    claim_details TEXT NOT NULL,
    resolution_status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    resolution_notes TEXT,
    resolved_by BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE correction_reports (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    target_entity_type VARCHAR(50) NOT NULL,
    target_public_id UUID NOT NULL,
    issue_summary TEXT NOT NULL,
    suggested_fix TEXT,
    resolution_status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    resolved_by BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_logs_created ON audit_logs(created_at DESC);
CREATE INDEX idx_public_submissions_status ON public_submissions(moderation_status);

