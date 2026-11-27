-- V3: Passages, Source-Specific Reference Hierarchies, Translations & Glossary
CREATE TABLE source_passages (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    source_edition_id BIGINT NOT NULL REFERENCES source_editions(id) ON DELETE RESTRICT,
    language_code VARCHAR(16) NOT NULL,
    text_layer_type VARCHAR(30) NOT NULL DEFAULT 'ORIGINAL_TEXT',
    original_text TEXT NOT NULL,
    context_before TEXT,
    context_after TEXT,
    printed_page_label VARCHAR(40),
    pdf_page_number INTEGER,
    extraction_confidence NUMERIC(5,2),
    ocr_uncertain BOOLEAN NOT NULL DEFAULT FALSE,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    created_by BIGINT REFERENCES users(id),
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    version INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_passage_layer CHECK (
        text_layer_type IN ('ORIGINAL_TEXT', 'PUBLISHED_TRANSLATION', 'COMMENTARY')
    ),
    CONSTRAINT chk_passage_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE source_references (
    id BIGSERIAL PRIMARY KEY,
    passage_id BIGINT NOT NULL REFERENCES source_passages(id) ON DELETE CASCADE,
    canonical_ref_key VARCHAR(160) NOT NULL,
    hierarchy_label_1 VARCHAR(60),
    hierarchy_value_1 VARCHAR(80),
    hierarchy_label_2 VARCHAR(60),
    hierarchy_value_2 VARCHAR(80),
    hierarchy_label_3 VARCHAR(60),
    hierarchy_value_3 VARCHAR(80),
    hierarchy_label_4 VARCHAR(60),
    hierarchy_value_4 VARCHAR(80),
    display_reference VARCHAR(255) NOT NULL
);

CREATE TABLE source_translations (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    original_passage_id BIGINT NOT NULL REFERENCES source_passages(id) ON DELETE CASCADE,
    target_language VARCHAR(16) NOT NULL,
    translated_text TEXT NOT NULL,
    translation_kind VARCHAR(30) NOT NULL DEFAULT 'HUMAN_TRANSLATION',
    translation_provider VARCHAR(80),
    translation_model VARCHAR(120),
    translation_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    reviewed_by BIGINT REFERENCES users(id),
    reviewed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_translation_kind CHECK (
        translation_kind IN ('PUBLISHED_HUMAN', 'HUMAN_TRANSLATION', 'MACHINE_DRAFT')
    ),
    CONSTRAINT chk_translation_status CHECK (
        translation_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE passage_relationships (
    id BIGSERIAL PRIMARY KEY,
    source_passage_id BIGINT NOT NULL REFERENCES source_passages(id) ON DELETE CASCADE,
    target_passage_id BIGINT NOT NULL REFERENCES source_passages(id) ON DELETE CASCADE,
    relationship_type VARCHAR(40) NOT NULL,
    notes TEXT,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE glossary_terms (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    term_en VARCHAR(180) NOT NULL,
    term_hi VARCHAR(180) NOT NULL,
    romanized_hi VARCHAR(180),
    definition TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_passages_status_lang ON source_passages(verification_status, language_code);
CREATE INDEX idx_passages_fts ON source_passages USING GIN (to_tsvector('simple', original_text));
CREATE INDEX idx_passages_trgm ON source_passages USING GIN (original_text gin_trgm_ops);
CREATE INDEX idx_source_refs_canonical ON source_references(canonical_ref_key);
CREATE INDEX idx_translations_passage_lang ON source_translations(original_passage_id, target_language);

