package com.platform.sources.dto;

import com.platform.common.audit.EvidenceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class SourceDtos {

    private SourceDtos() {}

    public record CreateSourceRequest(
            @NotBlank @Size(max = 300) String title,
            String alternateTitles,
            @NotBlank @Size(max = 50) String sourceType,
            @NotBlank @Size(max = 16) String primaryLanguage,
            String isbnOrIdentifier,
            String sourceUrl,
            String hostingOrganization
    ) {}

    public record CreateEditionRequest(
            @NotBlank @Size(max = 255) String editionName,
            String publisher,
            Integer publicationYear,
            String translatorName,
            @NotBlank @Size(max = 16) String languageCode,
            String isbn,
            String notes
    ) {}

    public record UpsertRightsRequest(
            @NotBlank @Size(max = 60) String copyrightStatus,
            String licenseName,
            String permissionDetails,
            @NotBlank String attributionRequirement,
            String accessRestrictions,
            boolean allowPublicExcerpt
    ) {}

    public record TransitionStatusRequest(
            EvidenceStatus targetStatus
    ) {}

    public record EditionView(
            UUID publicId,
            String editionName,
            String publisher,
            Integer publicationYear,
            String translatorName,
            String languageCode,
            String isbn
    ) {}

    public record RightsView(
            String copyrightStatus,
            String licenseName,
            String attributionRequirement,
            boolean allowPublicExcerpt
    ) {}

    public record SourceView(
            UUID publicId,
            String title,
            String alternateTitles,
            String sourceType,
            String primaryLanguage,
            String isbnOrIdentifier,
            String sourceUrl,
            String hostingOrganization,
            EvidenceStatus verificationStatus,
            Instant publishedAt,
            List<EditionView> editions,
            RightsView rights
    ) {}
}

