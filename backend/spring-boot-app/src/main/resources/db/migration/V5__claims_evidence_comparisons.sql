-- V5: Topics, Claims, Evidence Records & Comparative Research
CREATE TABLE topics (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    slug VARCHAR(120) NOT NULL UNIQUE,
    title_en VARCHAR(200) NOT NULL,
    title_hi VARCHAR(200) NOT NULL,
    description_en TEXT,
    description_hi TEXT
);

CREATE TABLE claims (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    topic_id BIGINT REFERENCES topics(id) ON DELETE SET NULL,
    statement_en TEXT NOT NULL,
    statement_hi TEXT,
    claim_origin_type VARCHAR(40) NOT NULL,
    origin_url TEXT,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_claims_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE evidence_records (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    source_edition_id BIGINT NOT NULL REFERENCES source_editions(id) ON DELETE RESTRICT,
    passage_id BIGINT REFERENCES source_passages(id) ON DELETE RESTRICT,
    teaching_id BIGINT REFERENCES teachings(id) ON DELETE RESTRICT,
    language_code VARCHAR(16) NOT NULL,
    excerpt_text TEXT NOT NULL,
    context_note TEXT,
    printed_page_label VARCHAR(40),
    pdf_page_number INTEGER,
    relationship_type VARCHAR(30) NOT NULL,
    rationale_en TEXT NOT NULL,
    rationale_hi TEXT,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_evidence_relationship CHECK (
        relationship_type IN ('SUPPORTING', 'CONTRADICTING', 'CONTEXTUAL', 'NEUTRAL', 'INCONCLUSIVE')
    ),
    CONSTRAINT chk_evidence_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE claim_evidence (
    claim_id BIGINT NOT NULL REFERENCES claims(id) ON DELETE CASCADE,
    evidence_id BIGINT NOT NULL REFERENCES evidence_records(id) ON DELETE CASCADE,
    display_order INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (claim_id, evidence_id)
);

CREATE TABLE comparisons (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    topic_id BIGINT REFERENCES topics(id) ON DELETE SET NULL,
    title_en VARCHAR(300) NOT NULL,
    title_hi VARCHAR(300),
    finding_category VARCHAR(40) NOT NULL,
    editorial_summary_en TEXT NOT NULL,
    editorial_summary_hi TEXT,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_comparison_finding CHECK (
        finding_category IN (
            'AGREEMENT', 'TRANSLATION_DIFFERENCE', 'INTERPRETATION_DIFFERENCE',
            'CONTRADICTION', 'MISSING_CONTEXT', 'INCONCLUSIVE'
        )
    ),
    CONSTRAINT chk_comparison_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE comparison_items (
    id BIGSERIAL PRIMARY KEY,
    comparison_id BIGINT NOT NULL REFERENCES comparisons(id) ON DELETE CASCADE,
    perspective_lane VARCHAR(40) NOT NULL,
    evidence_id BIGINT REFERENCES evidence_records(id) ON DELETE RESTRICT,
    perspective_title VARCHAR(255) NOT NULL,
    perspective_text_en TEXT NOT NULL,
    perspective_text_hi TEXT,
    display_order INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT chk_perspective_lane CHECK (
        perspective_lane IN ('ORIGINAL_SCRIPTURE', 'OTHER_SAINTS_COMMENTATORS', 'SANT_RAMPAL_JI_TEACHING')
    )
);

CREATE INDEX idx_claims_topic_status ON claims(topic_id, verification_status);
CREATE INDEX idx_evidence_status ON evidence_records(verification_status);
CREATE INDEX idx_comparisons_status ON comparisons(verification_status);

