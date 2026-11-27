package com.platform.sources.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "source_rights")
public class SourceRightsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

    @Column(name = "source_id", nullable = false)
    private Long sourceId;

    @Column(name = "copyright_status", nullable = false, length = 60)
    private String copyrightStatus;

    @Column(name = "license_name", length = 120)
    private String licenseName;

    @Column(name = "permission_details")
    private String permissionDetails;

    @Column(name = "attribution_requirement", nullable = false)
    private String attributionRequirement;

    @Column(name = "access_restrictions")
    private String accessRestrictions;

    @Column(name = "allow_public_excerpt", nullable = false)
    private boolean allowPublicExcerpt = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public Long getId() { return id; }
    public UUID getPublicId() { return publicId; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public String getCopyrightStatus() { return copyrightStatus; }
    public void setCopyrightStatus(String copyrightStatus) { this.copyrightStatus = copyrightStatus; }
    public String getLicenseName() { return licenseName; }
    public void setLicenseName(String licenseName) { this.licenseName = licenseName; }
    public String getPermissionDetails() { return permissionDetails; }
    public void setPermissionDetails(String permissionDetails) { this.permissionDetails = permissionDetails; }
    public String getAttributionRequirement() { return attributionRequirement; }
    public void setAttributionRequirement(String attributionRequirement) { this.attributionRequirement = attributionRequirement; }
    public String getAccessRestrictions() { return accessRestrictions; }
    public void setAccessRestrictions(String accessRestrictions) { this.accessRestrictions = accessRestrictions; }
    public boolean isAllowPublicExcerpt() { return allowPublicExcerpt; }
    public void setAllowPublicExcerpt(boolean allowPublicExcerpt) { this.allowPublicExcerpt = allowPublicExcerpt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

