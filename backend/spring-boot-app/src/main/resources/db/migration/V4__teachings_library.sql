-- V4: Teachings Library (Attributed Statements)
CREATE TABLE teachers (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    name_en VARCHAR(200) NOT NULL,
    name_hi VARCHAR(200) NOT NULL,
    tradition VARCHAR(120),
    bio_summary TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE teachings (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    teacher_id BIGINT NOT NULL REFERENCES teachers(id) ON DELETE RESTRICT,
    source_edition_id BIGINT REFERENCES source_editions(id) ON DELETE RESTRICT,
    title_en VARCHAR(300) NOT NULL,
    title_hi VARCHAR(300),
    statement_text TEXT NOT NULL,
    is_verbatim_quote BOOLEAN NOT NULL DEFAULT FALSE,
    original_language VARCHAR(16) NOT NULL,
    context_before TEXT,
    context_after TEXT,
    printed_page_label VARCHAR(40),
    pdf_page_number INTEGER,
    timestamp_seconds INTEGER,
    attribution_confidence VARCHAR(30) NOT NULL DEFAULT 'HIGH',
    verification_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_teachings_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE teaching_references (
    teaching_id BIGINT NOT NULL REFERENCES teachings(id) ON DELETE CASCADE,
    passage_id BIGINT NOT NULL REFERENCES source_passages(id) ON DELETE CASCADE,
    reference_note TEXT,
    PRIMARY KEY (teaching_id, passage_id)
);

CREATE INDEX idx_teachings_teacher_status ON teachings(teacher_id, verification_status);

