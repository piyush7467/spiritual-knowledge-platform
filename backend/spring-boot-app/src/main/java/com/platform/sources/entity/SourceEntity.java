package com.platform.sources.entity;

import com.platform.common.audit.EvidenceStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sources")
public class SourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false, length = 300)
    private String title;

    @Column(name = "alternate_titles")
    private String alternateTitles;

    @Column(name = "source_type", nullable = false, length = 50)
    private String sourceType;

    @Column(name = "primary_language", nullable = false, length = 16)
    private String primaryLanguage;

    @Column(name = "isbn_or_identifier", length = 80)
    private String isbnOrIdentifier;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "hosting_organization", length = 255)
    private String hostingOrganization;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    private EvidenceStatus verificationStatus = EvidenceStatus.REGISTERED;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public Long getId() { return id; }
    public UUID getPublicId() { return publicId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAlternateTitles() { return alternateTitles; }
    public void setAlternateTitles(String alternateTitles) { this.alternateTitles = alternateTitles; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getPrimaryLanguage() { return primaryLanguage; }
    public void setPrimaryLanguage(String primaryLanguage) { this.primaryLanguage = primaryLanguage; }
    public String getIsbnOrIdentifier() { return isbnOrIdentifier; }
    public void setIsbnOrIdentifier(String isbnOrIdentifier) { this.isbnOrIdentifier = isbnOrIdentifier; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public String getHostingOrganization() { return hostingOrganization; }
    public void setHostingOrganization(String hostingOrganization) { this.hostingOrganization = hostingOrganization; }
    public EvidenceStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(EvidenceStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public Long getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(Long verifiedBy) { this.verifiedBy = verifiedBy; }
    public Instant getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Instant verifiedAt) { this.verifiedAt = verifiedAt; }
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

