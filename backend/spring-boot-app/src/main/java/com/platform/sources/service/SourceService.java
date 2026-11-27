package com.platform.sources.service;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.audit.AuditLogService;
import com.platform.common.audit.EvidenceStatus;
import com.platform.sources.dto.SourceDtos.*;
import com.platform.sources.entity.SourceEditionEntity;
import com.platform.sources.entity.SourceEntity;
import com.platform.sources.entity.SourceRightsEntity;
import com.platform.sources.repository.SourceEditionRepository;
import com.platform.sources.repository.SourceRepository;
import com.platform.sources.repository.SourceRightsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SourceService implements SourceQueryPort {

    private final SourceRepository sourceRepository;
    private final SourceEditionRepository editionRepository;
    private final SourceRightsRepository rightsRepository;
    private final AuditLogService auditLogService;

    public SourceService(
            SourceRepository sourceRepository,
            SourceEditionRepository editionRepository,
            SourceRightsRepository rightsRepository,
            AuditLogService auditLogService
    ) {
        this.sourceRepository = sourceRepository;
        this.editionRepository = editionRepository;
        this.rightsRepository = rightsRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public SourceView createSource(CreateSourceRequest req, PlatformUserPrincipal actor) {
        SourceEntity source = new SourceEntity();
        source.setTitle(req.title().trim());
        source.setAlternateTitles(req.alternateTitles());
        source.setSourceType(req.sourceType().trim().toUpperCase());
        source.setPrimaryLanguage(req.primaryLanguage().trim().toLowerCase());
        source.setIsbnOrIdentifier(req.isbnOrIdentifier());
        source.setSourceUrl(req.sourceUrl());
        source.setHostingOrganization(req.hostingOrganization());
        source.setVerificationStatus(EvidenceStatus.REGISTERED);
        source.setCreatedBy(actor.userId());
        source.setUpdatedBy(actor.userId());
        source = sourceRepository.save(source);

        auditLogService.record(actor.userId(), actor.primaryRole(), "SOURCE_CREATED",
                "SOURCE", source.getPublicId(), Map.of("title", source.getTitle()));
        return toView(source);
    }

    @Transactional
    public EditionView addEdition(UUID sourcePublicId, CreateEditionRequest req, PlatformUserPrincipal actor) {
        SourceEntity source = sourceRepository.findByPublicId(sourcePublicId)
                .orElseThrow(() -> new IllegalArgumentException("Source not found"));
        SourceEditionEntity edition = new SourceEditionEntity();
        edition.setSourceId(source.getId());
        edition.setEditionName(req.editionName().trim());
        edition.setPublisher(req.publisher());
        edition.setPublicationYear(req.publicationYear());
        edition.setTranslatorName(req.translatorName());
        edition.setLanguageCode(req.languageCode().trim().toLowerCase());
        edition.setIsbn(req.isbn());
        edition.setNotes(req.notes());
        edition = editionRepository.save(edition);

        auditLogService.record(actor.userId(), actor.primaryRole(), "EDITION_ADDED",
                "SOURCE_EDITION", edition.getPublicId(), Map.of("sourcePublicId", sourcePublicId.toString()));
        return toEditionView(edition);
    }

    @Transactional
    public RightsView upsertRights(UUID sourcePublicId, UpsertRightsRequest req, PlatformUserPrincipal actor) {
        SourceEntity source = sourceRepository.findByPublicId(sourcePublicId)
                .orElseThrow(() -> new IllegalArgumentException("Source not found"));
        SourceRightsEntity rights = rightsRepository.findFirstBySourceId(source.getId())
                .orElseGet(SourceRightsEntity::new);
        rights.setSourceId(source.getId());
        rights.setCopyrightStatus(req.copyrightStatus().trim());
        rights.setLicenseName(req.licenseName());
        rights.setPermissionDetails(req.permissionDetails());
        rights.setAttributionRequirement(req.attributionRequirement().trim());
        rights.setAccessRestrictions(req.accessRestrictions());
        rights.setAllowPublicExcerpt(req.allowPublicExcerpt());
        rights.setUpdatedAt(Instant.now());
        rights = rightsRepository.save(rights);

        auditLogService.record(actor.userId(), actor.primaryRole(), "SOURCE_RIGHTS_UPDATED",
                "SOURCE", sourcePublicId, Map.of("copyrightStatus", rights.getCopyrightStatus()));
        return new RightsView(rights.getCopyrightStatus(), rights.getLicenseName(),
                rights.getAttributionRequirement(), rights.isAllowPublicExcerpt());
    }

    @Transactional(readOnly = true)
    public EvidenceStatus getStatus(UUID sourcePublicId) {
        return sourceRepository.findByPublicId(sourcePublicId)
                .map(SourceEntity::getVerificationStatus)
                .orElseThrow(() -> new IllegalArgumentException("Source not found"));
    }

    @Transactional
    public SourceView transitionStatus(UUID sourcePublicId, EvidenceStatus target, PlatformUserPrincipal actor) {
        if (target == null) {
            throw new IllegalArgumentException("Target status is required");
        }
        SourceEntity source = sourceRepository.findByPublicId(sourcePublicId)
                .orElseThrow(() -> new IllegalArgumentException("Source not found"));
        EvidenceStatus previous = source.getVerificationStatus();
        source.setVerificationStatus(target);
        source.setUpdatedBy(actor.userId());
        source.setUpdatedAt(Instant.now());

        if (target == EvidenceStatus.VERIFIED || target == EvidenceStatus.PUBLISHED) {
            source.setVerifiedBy(actor.userId());
            source.setVerifiedAt(Instant.now());
        }
        if (target == EvidenceStatus.PUBLISHED) {
            source.setPublishedAt(Instant.now());
        }
        source = sourceRepository.save(source);

        auditLogService.record(actor.userId(), actor.primaryRole(), "SOURCE_STATUS_CHANGED",
                "SOURCE", sourcePublicId, Map.of("from", previous.name(), "to", target.name()));
        return toView(source);
    }

    @Transactional(readOnly = true)
    public Page<SourceView> listPublished(Pageable pageable) {
        return sourceRepository.findAllByVerificationStatus(EvidenceStatus.PUBLISHED, pageable)
                .map(this::toView);
    }

    @Transactional(readOnly = true)
    public SourceView getPublished(UUID sourcePublicId) {
        SourceEntity source = sourceRepository
                .findByPublicIdAndVerificationStatus(sourcePublicId, EvidenceStatus.PUBLISHED)
                .orElseThrow(() -> new IllegalArgumentException("Published source not found"));
        return toView(source);
    }

    @Transactional(readOnly = true)
    public Page<SourceView> listAllForAdmin(Pageable pageable) {
        return sourceRepository.findAll(pageable).map(this::toView);
    }

    @Override
    @Transactional(readOnly = true)
    public EditionProvenance resolveEditionByPublicId(UUID editionPublicId) {
        SourceEditionEntity edition = editionRepository.findByPublicId(editionPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Source edition not found"));
        return buildProvenance(edition);
    }

    @Override
    @Transactional(readOnly = true)
    public EditionProvenance getEditionProvenanceById(Long editionId) {
        SourceEditionEntity edition = editionRepository.findById(editionId)
                .orElseThrow(() -> new IllegalArgumentException("Source edition not found"));
        return buildProvenance(edition);
    }

    private EditionProvenance buildProvenance(SourceEditionEntity edition) {
        SourceEntity source = sourceRepository.findById(edition.getSourceId())
                .orElseThrow(() -> new IllegalStateException("Orphan source edition"));
        String attribution = rightsRepository.findFirstBySourceId(source.getId())
                .map(SourceRightsEntity::getAttributionRequirement)
                .orElse("");
        return new EditionProvenance(
                edition.getId(),
                edition.getPublicId(),
                source.getPublicId(),
                source.getTitle(),
                source.getSourceType(),
                edition.getEditionName(),
                edition.getPublisher(),
                edition.getPublicationYear(),
                edition.getLanguageCode(),
                attribution,
                source.getVerificationStatus() == EvidenceStatus.PUBLISHED
        );
    }

    private SourceView toView(SourceEntity source) {
        List<EditionView> editions = editionRepository.findAllBySourceId(source.getId())
                .stream().map(this::toEditionView).toList();
        RightsView rights = rightsRepository.findFirstBySourceId(source.getId())
                .map(r -> new RightsView(r.getCopyrightStatus(), r.getLicenseName(),
                        r.getAttributionRequirement(), r.isAllowPublicExcerpt()))
                .orElse(null);
        return new SourceView(
                source.getPublicId(),
                source.getTitle(),
                source.getAlternateTitles(),
                source.getSourceType(),
                source.getPrimaryLanguage(),
                source.getIsbnOrIdentifier(),
                source.getSourceUrl(),
                source.getHostingOrganization(),
                source.getVerificationStatus(),
                source.getPublishedAt(),
                editions,
                rights
        );
    }

    private EditionView toEditionView(SourceEditionEntity e) {
        return new EditionView(e.getPublicId(), e.getEditionName(), e.getPublisher(),
                e.getPublicationYear(), e.getTranslatorName(), e.getLanguageCode(), e.getIsbn());
    }
}

