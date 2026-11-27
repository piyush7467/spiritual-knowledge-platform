-- V6: Video Library & Integer-Second Timestamped Segments
CREATE TABLE videos (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    platform_name VARCHAR(50) NOT NULL,
    original_url TEXT NOT NULL,
    embed_url TEXT,
    embed_permitted BOOLEAN NOT NULL DEFAULT FALSE,
    supports_timestamp_playback BOOLEAN NOT NULL DEFAULT FALSE,
    title_en VARCHAR(300) NOT NULL,
    title_hi VARCHAR(300),
    speaker_name VARCHAR(200),
    language_code VARCHAR(16) NOT NULL,
    duration_seconds INTEGER,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_videos_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE video_segments (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    video_id BIGINT NOT NULL REFERENCES videos(id) ON DELETE CASCADE,
    topic_id BIGINT REFERENCES topics(id) ON DELETE SET NULL,
    title_en VARCHAR(300) NOT NULL,
    title_hi VARCHAR(300),
    summary_en TEXT,
    summary_hi TEXT,
    start_seconds INTEGER NOT NULL,
    end_seconds INTEGER NOT NULL,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'NEEDS_REVIEW',
    verified_by BIGINT REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_segment_bounds CHECK (start_seconds >= 0 AND end_seconds > start_seconds),
    CONSTRAINT chk_segment_status CHECK (
        verification_status IN (
            'DISCOVERED', 'REGISTERED', 'PROCESSING', 'NEEDS_REVIEW',
            'VERIFIED', 'PUBLISHED', 'REJECTED', 'NEEDS_CORRECTION', 'ARCHIVED'
        )
    )
);

CREATE TABLE video_segment_references (
    segment_id BIGINT NOT NULL REFERENCES video_segments(id) ON DELETE CASCADE,
    passage_id BIGINT REFERENCES source_passages(id) ON DELETE CASCADE,
    claim_id BIGINT REFERENCES claims(id) ON DELETE CASCADE,
    notes TEXT,
    PRIMARY KEY (segment_id, passage_id, claim_id)
);

CREATE INDEX idx_video_segments_video ON video_segments(video_id, verification_status);

