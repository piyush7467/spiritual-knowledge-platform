-- V2: Sources, Editions, Files, Rights, and Authors
CREATE TABLE sources (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    title VARCHAR(300) NOT NULL,
    alternate_titles TEXT,
    source_type VARCHAR(50) NOT NULL,
    primary_language VARCHAR(16) NOT NULL,
    isbn_or_identifier VARCHAR(80),
    source_url TEXT,
    hosting_organization VARCHAR(255),
    verification_status VARCHAR(30) NOT NULL DEFAULT 'REGISTERED',
    created_by BIGINT REFERENCES users(id),
    updated_by BIGINT REFERENCES users(id),
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_sources_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE source_editions (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    source_id BIGINT NOT NULL REFERENCES sources(id) ON DELETE CASCADE,
    edition_name VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    publication_year INTEGER,
    translator_name VARCHAR(255),
    language_code VARCHAR(16) NOT NULL,
    isbn VARCHAR(80),
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE source_files (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    source_edition_id BIGINT NOT NULL REFERENCES source_editions(id) ON DELETE CASCADE,
    file_role VARCHAR(40) NOT NULL,
    storage_provider VARCHAR(40) NOT NULL,
    storage_key TEXT NOT NULL,
    mime_type VARCHAR(120) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    sha256_checksum VARCHAR(64) NOT NULL,
    page_count INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE source_rights (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    source_id BIGINT NOT NULL REFERENCES sources(id) ON DELETE CASCADE,
    copyright_status VARCHAR(60) NOT NULL,
    license_name VARCHAR(120),
    permission_details TEXT,
    attribution_requirement TEXT NOT NULL,
    access_restrictions TEXT,
    allow_public_excerpt BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE source_authors (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    name VARCHAR(200) NOT NULL,
    author_type VARCHAR(40) NOT NULL DEFAULT 'PERSON',
    bio_summary TEXT
);

CREATE TABLE source_author_links (
    source_id BIGINT NOT NULL REFERENCES sources(id) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES source_authors(id) ON DELETE CASCADE,
    attribution_role VARCHAR(50) NOT NULL DEFAULT 'AUTHOR',
    PRIMARY KEY (source_id, author_id, attribution_role)
);

CREATE INDEX idx_sources_status_type ON sources(verification_status, source_type);
CREATE INDEX idx_sources_language ON sources(primary_language);

