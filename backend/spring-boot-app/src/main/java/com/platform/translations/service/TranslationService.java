package com.platform.translations.service;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.audit.AuditLogService;
import com.platform.common.audit.EvidenceStatus;
import com.platform.translations.entity.SourceTranslationEntity;
import com.platform.translations.repository.SourceTranslationRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TranslationService implements TranslationQueryPort {

    private final SourceTranslationRepository translationRepository;
    private final AuditLogService auditLogService;

    public TranslationService(
            SourceTranslationRepository translationRepository,
            AuditLogService auditLogService
    ) {
        this.translationRepository = translationRepository;
        this.auditLogService = auditLogService;
    }

    public record CreateTranslationRequest(
            Long originalPassageId,
            @NotBlank @Size(max = 16) String targetLanguage,
            @NotBlank String translatedText,
            @NotBlank String translationKind,
            String translationProvider,
            String translationModel
    ) {}

    @Transactional
    public TranslationSummary createTranslation(CreateTranslationRequest req, PlatformUserPrincipal actor) {
        SourceTranslationEntity entity = new SourceTranslationEntity();
        entity.setOriginalPassageId(req.originalPassageId());
        entity.setTargetLanguage(req.targetLanguage().trim().toLowerCase());
        entity.setTranslatedText(req.translatedText().trim());
        entity.setTranslationKind(req.translationKind().trim().toUpperCase());
        entity.setTranslationProvider(req.translationProvider());
        entity.setTranslationModel(req.translationModel());
        entity.setTranslationStatus(EvidenceStatus.NEEDS_REVIEW);
        entity = translationRepository.save(entity);

        auditLogService.record(actor.userId(), actor.primaryRole(), "TRANSLATION_CREATED",
                "TRANSLATION", entity.getPublicId(), Map.of("targetLanguage", entity.getTargetLanguage()));
        return toSummary(entity);
    }

    @Transactional
    public TranslationSummary publishTranslation(UUID translationPublicId, PlatformUserPrincipal actor) {
        SourceTranslationEntity entity = translationRepository.findByPublicId(translationPublicId)
                .orElseThrow(() -> new IllegalArgumentException("Translation not found"));
        entity.setTranslationStatus(EvidenceStatus.PUBLISHED);
        entity.setReviewedBy(actor.userId());
        entity.setReviewedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity = translationRepository.save(entity);

        auditLogService.record(actor.userId(), actor.primaryRole(), "TRANSLATION_PUBLISHED",
                "TRANSLATION", translationPublicId, Map.of());
        return toSummary(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TranslationSummary> findPublishedByPassageId(Long passageId) {
        return translationRepository
                .findAllByOriginalPassageIdAndTranslationStatus(passageId, EvidenceStatus.PUBLISHED)
                .stream().map(this::toSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TranslationSummary> findAllByPassageId(Long passageId) {
        return translationRepository.findAllByOriginalPassageId(passageId)
                .stream().map(this::toSummary).toList();
    }

    private TranslationSummary toSummary(SourceTranslationEntity e) {
        return new TranslationSummary(
                e.getPublicId(),
                e.getTargetLanguage(),
                e.getTranslatedText(),
                e.getTranslationKind(),
                e.getTranslationProvider(),
                e.getTranslationStatus()
        );
    }
}

