package com.platform.translations.entity;

import com.platform.common.audit.EvidenceStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "source_translations")
public class SourceTranslationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

    @Column(name = "original_passage_id", nullable = false)
    private Long originalPassageId;

    @Column(name = "target_language", nullable = false, length = 16)
    private String targetLanguage;

    @Column(name = "translated_text", nullable = false)
    private String translatedText;

    @Column(name = "translation_kind", nullable = false, length = 30)
    private String translationKind = "HUMAN_TRANSLATION";

    @Column(name = "translation_provider", length = 80)
    private String translationProvider;

    @Column(name = "translation_model", length = 120)
    private String translationModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "translation_status", nullable = false, length = 30)
    private EvidenceStatus translationStatus = EvidenceStatus.NEEDS_REVIEW;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public Long getId() { return id; }
    public UUID getPublicId() { return publicId; }
    public Long getOriginalPassageId() { return originalPassageId; }
    public void setOriginalPassageId(Long originalPassageId) { this.originalPassageId = originalPassageId; }
    public String getTargetLanguage() { return targetLanguage; }
    public void setTargetLanguage(String targetLanguage) { this.targetLanguage = targetLanguage; }
    public String getTranslatedText() { return translatedText; }
    public void setTranslatedText(String translatedText) { this.translatedText = translatedText; }
    public String getTranslationKind() { return translationKind; }
    public void setTranslationKind(String translationKind) { this.translationKind = translationKind; }
    public String getTranslationProvider() { return translationProvider; }
    public void setTranslationProvider(String translationProvider) { this.translationProvider = translationProvider; }
    public String getTranslationModel() { return translationModel; }
    public void setTranslationModel(String translationModel) { this.translationModel = translationModel; }
    public EvidenceStatus getTranslationStatus() { return translationStatus; }
    public void setTranslationStatus(EvidenceStatus translationStatus) { this.translationStatus = translationStatus; }
    public Long getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }
    public Instant getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Instant reviewedAt) { this.reviewedAt = reviewedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

