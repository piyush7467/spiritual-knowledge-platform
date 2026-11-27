package com.platform.common.audit;

/**
 * Unified evidence and publishing lifecycle states across sources, passages,
 * teachings, claims, evidence records, comparisons, and video segments.
 * VERIFIED means checked against the stated source edition, never a religious truth claim.
 */
public enum EvidenceStatus {
    DISCOVERED,
    REGISTERED,
    PROCESSING,
    NEEDS_REVIEW,
    VERIFIED,
    PUBLISHED,
    REJECTED,
    NEEDS_CORRECTION,
    ARCHIVED
}

