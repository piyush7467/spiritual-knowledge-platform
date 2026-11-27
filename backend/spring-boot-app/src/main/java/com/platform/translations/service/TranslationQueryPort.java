package com.platform.translations.service;

import com.platform.common.audit.EvidenceStatus;
import java.util.List;
import java.util.UUID;

/**
 * Public cross-module query interface for passage translations.
 */
public interface TranslationQueryPort {
    List<TranslationSummary> findPublishedByPassageId(Long passageId);
    List<TranslationSummary> findAllByPassageId(Long passageId);

    record TranslationSummary(
            UUID publicId,
            String targetLanguage,
            String translatedText,
            String translationKind,
            String translationProvider,
            EvidenceStatus translationStatus
    ) {}
}

