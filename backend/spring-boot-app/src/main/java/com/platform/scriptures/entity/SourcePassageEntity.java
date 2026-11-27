package com.platform.scriptures.entity;

import com.platform.common.audit.EvidenceStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "source_passages")
public class SourcePassageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

    @Column(name = "source_edition_id", nullable = false)
    private Long sourceEditionId;

    @Column(name = "language_code", nullable = false, length = 16)
    private String languageCode;

    @Column(name = "text_layer_type", nullable = false, length = 30)
    private String textLayerType = "ORIGINAL_TEXT";

    @Column(name = "original_text", nullable = false)
    private String originalText;

    @Column(name = "context_before")
    private String contextBefore;

    @Column(name = "context_after")
    private String contextAfter;

    @Column(name = "printed_page_label", length = 40)
    private String printedPageLabel;

    @Column(name = "pdf_page_number")
    private Integer pdfPageNumber;

    @Column(name = "extraction_confidence", precision = 5, scale = 2)
    private BigDecimal extractionConfidence;

    @Column(name = "ocr_uncertain", nullable = false)
    private boolean ocrUncertain = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    private EvidenceStatus verificationStatus = EvidenceStatus.NEEDS_REVIEW;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(nullable = false)
    private int version = 1;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public Long getId() { return id; }
    public UUID getPublicId() { return publicId; }
    public Long getSourceEditionId() { return sourceEditionId; }
    public void setSourceEditionId(Long sourceEditionId) { this.sourceEditionId = sourceEditionId; }
    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }
    public String getTextLayerType() { return textLayerType; }
    public void setTextLayerType(String textLayerType) { this.textLayerType = textLayerType; }
    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }
    public String getContextBefore() { return contextBefore; }
    public void setContextBefore(String contextBefore) { this.contextBefore = contextBefore; }
    public String getContextAfter() { return contextAfter; }
    public void setContextAfter(String contextAfter) { this.contextAfter = contextAfter; }
    public String getPrintedPageLabel() { return printedPageLabel; }
    public void setPrintedPageLabel(String printedPageLabel) { this.printedPageLabel = printedPageLabel; }
    public Integer getPdfPageNumber() { return pdfPageNumber; }
    public void setPdfPageNumber(Integer pdfPageNumber) { this.pdfPageNumber = pdfPageNumber; }
    public BigDecimal getExtractionConfidence() { return extractionConfidence; }
    public void setExtractionConfidence(BigDecimal extractionConfidence) { this.extractionConfidence = extractionConfidence; }
    public boolean isOcrUncertain() { return ocrUncertain; }
    public void setOcrUncertain(boolean ocrUncertain) { this.ocrUncertain = ocrUncertain; }
    public EvidenceStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(EvidenceStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(Long verifiedBy) { this.verifiedBy = verifiedBy; }
    public Instant getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Instant verifiedAt) { this.verifiedAt = verifiedAt; }
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

