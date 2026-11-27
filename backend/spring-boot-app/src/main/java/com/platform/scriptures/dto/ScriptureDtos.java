package com.platform.scriptures.dto;

import com.platform.common.audit.EvidenceStatus;
import com.platform.translations.service.TranslationQueryPort.TranslationSummary;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class ScriptureDtos {

    private ScriptureDtos() {}

    public record ReferenceInput(
            @NotBlank String canonicalRefKey,
            String hierarchyLabel1,
            String hierarchyValue1,
            String hierarchyLabel2,
            String hierarchyValue2,
            String hierarchyLabel3,
            String hierarchyValue3,
            String hierarchyLabel4,
            String hierarchyValue4,
            @NotBlank String displayReference
    ) {}

    public record CreatePassageRequest(
            @NotNull UUID editionPublicId,
            @NotBlank String languageCode,
            @NotBlank String textLayerType,
            @NotBlank String originalText,
            String contextBefore,
            String contextAfter,
            String printedPageLabel,
            Integer pdfPageNumber,
            BigDecimal extractionConfidence,
            boolean ocrUncertain,
            ReferenceInput reference
    ) {}

    public record PassageStatusRequest(@NotNull EvidenceStatus targetStatus) {}

    public record ReferenceView(
            String canonicalRefKey,
            String displayReference,
            String hierarchyLabel1,
            String hierarchyValue1,
            String hierarchyLabel2,
            String hierarchyValue2,
            String hierarchyLabel3,
            String hierarchyValue3
    ) {}

    public record PassageView(
            UUID publicId,
            UUID sourcePublicId,
            String sourceTitle,
            String editionName,
            String publisher,
            String attributionRequirement,
            String languageCode,
            String textLayerType,
            String originalText,
            String contextBefore,
            String contextAfter,
            String printedPageLabel,
            Integer pdfPageNumber,
            boolean ocrUncertain,
            EvidenceStatus verificationStatus,
            Instant publishedAt,
            List<ReferenceView> references,
            List<TranslationSummary> translations
    ) {}
}

