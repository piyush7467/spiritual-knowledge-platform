package com.platform.sources.service;

import java.util.UUID;

/**
 * Public service boundary exposed by the Sources module to Scriptures, Teachings, and Evidence.
 */
public interface SourceQueryPort {
    EditionProvenance resolveEditionByPublicId(UUID editionPublicId);
    EditionProvenance getEditionProvenanceById(Long editionId);

    record EditionProvenance(
            Long editionId,
            UUID editionPublicId,
            UUID sourcePublicId,
            String sourceTitle,
            String sourceType,
            String editionName,
            String publisher,
            Integer publicationYear,
            String languageCode,
            String attributionRequirement,
            boolean sourcePublished
    ) {}
}

