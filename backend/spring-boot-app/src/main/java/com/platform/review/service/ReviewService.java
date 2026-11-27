package com.platform.review.service;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.audit.AuditLogService;
import com.platform.common.audit.EvidenceStatus;
import com.platform.review.entity.ContentVersionEntity;
import com.platform.review.entity.ReviewTaskEntity;
import com.platform.review.entity.VerificationReviewEntity;
import com.platform.review.repository.ContentVersionRepository;
import com.platform.review.repository.ReviewTaskRepository;
import com.platform.review.repository.VerificationReviewRepository;
import com.platform.scriptures.service.ScriptureService;
import com.platform.sources.service.SourceService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewTaskRepository taskRepository;
    private final VerificationReviewRepository reviewRepository;
    private final ContentVersionRepository versionRepository;
    private final SourceService sourceService;
    private final ScriptureService scriptureService;
    private final AuditLogService auditLogService;

    public ReviewService(
            ReviewTaskRepository taskRepository,
            VerificationReviewRepository reviewRepository,
            ContentVersionRepository versionRepository,
            SourceService sourceService,
            ScriptureService scriptureService,
            AuditLogService auditLogService
    ) {
        this.taskRepository = taskRepository;
        this.reviewRepository = reviewRepository;
        this.versionRepository = versionRepository;
        this.sourceService = sourceService;
        this.scriptureService = scriptureService;
        this.auditLogService = auditLogService;
    }

    public record ReviewDecisionRequest(
            @NotNull EvidenceStatus newStatus,
            @NotBlank String rationale
    ) {}

    public record ReviewTaskView(
            UUID publicId,
            String entityType,
            UUID entityPublicId,
            int priority,
            String taskStatus,
            Instant createdAt
    ) {}

    @Transactional
    public ReviewTaskView enqueueTask(String entityType, UUID entityPublicId, int priority) {
        ReviewTaskEntity task = new ReviewTaskEntity();
        task.setEntityType(entityType.toUpperCase());
        task.setEntityPublicId(entityPublicId);
        task.setPriority(Math.clamp(priority, 1, 5));
        task.setTaskStatus("OPEN");
        task = taskRepository.save(task);
        return toView(task);
    }

    @Transactional(readOnly = true)
    public Page<ReviewTaskView> listOpenTasks(Pageable pageable) {
        return taskRepository.findAllByTaskStatusOrderByPriorityAscCreatedAtAsc("OPEN", pageable)
                .map(this::toView);
    }

    @Transactional(readOnly = true)
    public List<VerificationReviewEntity> getHistory(String entityType, UUID entityPublicId) {
        return reviewRepository.findAllByEntityTypeAndEntityPublicIdOrderByReviewedAtDesc(
                entityType.toUpperCase(), entityPublicId
        );
    }

    @Transactional
    public VerificationReviewEntity submitDecision(
            String entityType,
            UUID entityPublicId,
            ReviewDecisionRequest req,
            PlatformUserPrincipal actor
    ) {
        String normalizedType = entityType.trim().toUpperCase();
        String previousStatus;
        Map<String, Object> snapshot;

        switch (normalizedType) {
            case "SOURCE" -> {
                previousStatus = sourceService.getStatus(entityPublicId).name();
                var updated = sourceService.transitionStatus(entityPublicId, req.newStatus(), actor);
                snapshot = Map.of(
                        "title", updated.title(),
                        "status", updated.verificationStatus().name(),
                        "sourceType", updated.sourceType()
                );
            }
            case "PASSAGE" -> {
                previousStatus = scriptureService.getStatus(entityPublicId).name();
                var updated = scriptureService.transitionStatus(entityPublicId, req.newStatus(), actor);
                snapshot = Map.of(
                        "sourceTitle", updated.sourceTitle(),
                        "status", updated.verificationStatus().name(),
                        "printedPage", updated.printedPageLabel() != null ? updated.printedPageLabel() : ""
                );
            }
            default -> throw new IllegalArgumentException("Unsupported review entity type: " + entityType);
        }

        VerificationReviewEntity review = new VerificationReviewEntity();
        review.setEntityType(normalizedType);
        review.setEntityPublicId(entityPublicId);
        review.setPreviousStatus(previousStatus);
        review.setNewStatus(req.newStatus().name());
        review.setReviewerId(actor.userId());
        review.setRationale(req.rationale().trim());
        review = reviewRepository.save(review);

        int nextVersion = versionRepository
                .findTopByEntityTypeAndEntityPublicIdOrderByVersionNumberDesc(normalizedType, entityPublicId)
                .map(v -> v.getVersionNumber() + 1)
                .orElse(1);

        ContentVersionEntity version = new ContentVersionEntity();
        version.setEntityType(normalizedType);
        version.setEntityPublicId(entityPublicId);
        version.setVersionNumber(nextVersion);
        version.setSnapshotJson(snapshot);
        version.setChangedBy(actor.userId());
        versionRepository.save(version);

        taskRepository.findFirstByEntityTypeAndEntityPublicIdAndTaskStatus(normalizedType, entityPublicId, "OPEN")
                .ifPresent(task -> {
                    task.setTaskStatus("COMPLETED");
                    task.setUpdatedAt(Instant.now());
                    taskRepository.save(task);
                });

        auditLogService.record(actor.userId(), actor.primaryRole(), "REVIEW_DECISION_SUBMITTED",
                normalizedType, entityPublicId, Map.of("newStatus", req.newStatus().name(), "version", nextVersion));
        return review;
    }

    private ReviewTaskView toView(ReviewTaskEntity t) {
        return new ReviewTaskView(t.getPublicId(), t.getEntityType(), t.getEntityPublicId(),
                t.getPriority(), t.getTaskStatus(), t.getCreatedAt());
    }
}
