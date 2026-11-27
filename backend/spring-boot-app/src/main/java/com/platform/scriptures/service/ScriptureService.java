package com.platform.scriptures.service;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.audit.AuditLogService;
import com.platform.common.audit.EvidenceStatus;
import com.platform.scriptures.dto.ScriptureDtos.*;
import com.platform.scriptures.entity.SourcePassageEntity;
import com.platform.scriptures.entity.SourceReferenceEntity;
import com.platform.scriptures.repository.SourcePassageRepository;
import com.platform.scriptures.repository.SourceReferenceRepository;
import com.platform.sources.service.SourceQueryPort;
import com.platform.sources.service.SourceQueryPort.EditionProvenance;
import com.platform.translations.service.TranslationQueryPort;
import com.platform.translations.service.TranslationQueryPort.TranslationSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ScriptureService {

    private final SourcePassageRepository passageRepository;
    private final SourceReferenceRepository referenceRepository;
    private final SourceQueryPort sourceQueryPort;
    private final TranslationQueryPort translationQueryPort;
    private final AuditLogService auditLogService;

    public ScriptureService(
            SourcePassageRepository passageRepository,
            SourceReferenceRepository referenceRepository,
            SourceQueryPort sourceQueryPort,
            TranslationQueryPort translationQueryPort,
            AuditLogService auditLogService
    ) {
        this.passageRepository = passageRepository;
        this.referenceRepository = referenceRepository;
        this.sourceQueryPort = sourceQueryPort;
        this.translationQueryPort = translationQueryPort;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public PassageView createPassage(CreatePassageRequest req, PlatformUserPrincipal actor) {
        EditionProvenance edition = sourceQueryPort.resolveEditionByPublicId(req.editionPublicId());

        SourcePassageEntity passage = new SourcePassageEntity();
        passage.setSourceEditionId(edition.editionId());
        passage.setLanguageCode(req.languageCode().trim().toLowerCase());
        passage.setTextLayerType(req.textLayerType().trim().toUpperCase());
        passage.setOriginalText(req.originalText());
        passage.setContextBefore(req.contextBefore());
        passage.setContextAfter(req.contextAfter());
        passage.setPrintedPageLabel(req.printedPageLabel());
        passage.setPdfPageNumber(req.pdfPageNumber());
        passage.setExtractionConfidence(req.extractionConfidence());
        passage.setOcrUncertain(req.ocrUncertain());
        passage.setVerificationStatus(EvidenceStatus.NEEDS_REVIEW);
        passage.setCreatedBy(actor.userId());
        passage = passageRepository.save(passage);

        if (req.reference() != null) {
            SourceReferenceEntity ref = new SourceReferenceEntity();
            ref.setPassageId(passage.getId());
            ref.setCanonicalRefKey(req.reference().canonicalRefKey().trim().toUpperCase());
            ref.setHierarchyLabel1(req.reference().hierarchyLabel1());
            ref.setHierarchyValue1(req.reference().hierarchyValue1());
            ref.setHierarchyLabel2(req.reference().hierarchyLabel2());
            ref.setHierarchyValue2(req.reference().hierarchyValue2());
            ref.setHierarchyLabel3(req.reference().hierarchyLabel3());
            ref.setHierarchyValue3(req.reference().hierarchyValue3());
            ref.setHierarchyLabel4(req.reference().hierarchyLabel4());
            ref.setHierarchyValue4(req.reference().hierarchyValue4());
            ref.setDisplayReference(req.reference().displayReference().trim());
            referenceRepository.save(ref);
        }

        auditLogService.record(actor.userId(), actor.primaryRole(), "PASSAGE_CREATED",
                "PASSAGE", passage.getPublicId(), Map.of("editionPublicId", req.editionPublicId().toString()));
        return toView(passage, edition, false);
    }

    @Transactional(readOnly = true)
    public EvidenceStatus getStatus(UUID passagePublicId) {
        return passageRepository.findByPublicId(passagePublicId)
                .map(SourcePassageEntity::getVerificationStatus)
                .orElseThrow(() -> new IllegalArgumentException("Passage not found"));
    }

    @Transactional
    public PassageView transitionStatus(UUID passagePublicId, EvidenceStatus target, PlatformUserPrincipal actor) {
        SourcePassageEntity passage = passageRepository.findByPublicId(passagePublicId)
                .orElseThrow(() -> new IllegalArgumentException("Passage not found"));
        EvidenceStatus previous = passage.getVerificationStatus();
        passage.setVerificationStatus(target);
        passage.setUpdatedAt(Instant.now());

        if (target == EvidenceStatus.VERIFIED || target == EvidenceStatus.PUBLISHED) {
            passage.setVerifiedBy(actor.userId());
            passage.setVerifiedAt(Instant.now());
        }
        if (target == EvidenceStatus.PUBLISHED) {
            passage.setPublishedAt(Instant.now());
        }
        passage = passageRepository.save(passage);

        auditLogService.record(actor.userId(), actor.primaryRole(), "PASSAGE_STATUS_CHANGED",
                "PASSAGE", passagePublicId, Map.of("from", previous.name(), "to", target.name()));
        EditionProvenance edition = sourceQueryPort.getEditionProvenanceById(passage.getSourceEditionId());
        return toView(passage, edition, false);
    }

    @Transactional(readOnly = true)
    public Page<PassageView> listPublished(Pageable pageable) {
        return passageRepository.findAllByVerificationStatus(EvidenceStatus.PUBLISHED, pageable)
                .map(p -> toView(p, sourceQueryPort.getEditionProvenanceById(p.getSourceEditionId()), true));
    }

    @Transactional(readOnly = true)
    public PassageView getPublished(UUID passagePublicId) {
        SourcePassageEntity passage = passageRepository
                .findByPublicIdAndVerificationStatus(passagePublicId, EvidenceStatus.PUBLISHED)
                .orElseThrow(() -> new IllegalArgumentException("Published passage not found"));
        EditionProvenance edition = sourceQueryPort.getEditionProvenanceById(passage.getSourceEditionId());
        return toView(passage, edition, true);
    }

    private PassageView toView(SourcePassageEntity passage, EditionProvenance edition, boolean publicOnly) {
        List<ReferenceView> refs = referenceRepository.findAllByPassageId(passage.getId())
                .stream()
                .map(r -> new ReferenceView(
                        r.getCanonicalRefKey(),
                        r.getDisplayReference(),
                        r.getHierarchyLabel1(),
                        r.getHierarchyValue1(),
                        r.getHierarchyLabel2(),
                        r.getHierarchyValue2(),
                        r.getHierarchyLabel3(),
                        r.getHierarchyValue3()
                ))
                .toList();

        List<TranslationSummary> translations = publicOnly
                ? translationQueryPort.findPublishedByPassageId(passage.getId())
                : translationQueryPort.findAllByPassageId(passage.getId());

        return new PassageView(
                passage.getPublicId(),
                edition.sourcePublicId(),
                edition.sourceTitle(),
                edition.editionName(),
                edition.publisher(),
                edition.attributionRequirement(),
                passage.getLanguageCode(),
                passage.getTextLayerType(),
                passage.getOriginalText(),
                passage.getContextBefore(),
                passage.getContextAfter(),
                passage.getPrintedPageLabel(),
                passage.getPdfPageNumber(),
                passage.isOcrUncertain(),
                passage.getVerificationStatus(),
                passage.getPublishedAt(),
                refs,
                translations
        );
    }
}

