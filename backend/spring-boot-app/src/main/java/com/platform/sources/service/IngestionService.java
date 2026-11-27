package com.platform.sources.service;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.audit.AuditLogService;
import com.platform.sources.entity.IngestionJobEntity;
import com.platform.sources.entity.SourceFileEntity;
import com.platform.sources.repository.IngestionJobRepository;
import com.platform.sources.repository.SourceFileRepository;
import com.platform.sources.service.SourceQueryPort.EditionProvenance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class IngestionService {

    private final SourceFileRepository fileRepository;
    private final IngestionJobRepository jobRepository;
    private final SourceQueryPort sourceQueryPort;
    private final UploadSafetyValidator safetyValidator;
    private final AuditLogService auditLogService;

    public IngestionService(
            SourceFileRepository fileRepository,
            IngestionJobRepository jobRepository,
            SourceQueryPort sourceQueryPort,
            UploadSafetyValidator safetyValidator,
            AuditLogService auditLogService
    ) {
        this.fileRepository = fileRepository;
        this.jobRepository = jobRepository;
        this.sourceQueryPort = sourceQueryPort;
        this.safetyValidator = safetyValidator;
        this.auditLogService = auditLogService;
    }

    public record RegisterFileAndQueueRequest(
            @NotNull UUID editionPublicId,
            @NotBlank String fileRole,
            @NotBlank String storageProvider,
            @NotBlank String storageKey,
            @NotBlank String mimeType,
            long fileSizeBytes,
            @NotBlank String sha256Checksum,
            Integer pageCount
    ) {}

    public record IngestionJobView(
            UUID jobPublicId,
            UUID filePublicId,
            String jobStatus,
            int attemptCount,
            String errorMessage
    ) {}

    @Transactional
    public IngestionJobView registerFileAndQueueJob(
            RegisterFileAndQueueRequest req,
            PlatformUserPrincipal actor
    ) {
        safetyValidator.validateFileMetadata(req.mimeType(), req.fileSizeBytes());
        EditionProvenance edition = sourceQueryPort.resolveEditionByPublicId(req.editionPublicId());

        SourceFileEntity file = fileRepository
                .findFirstBySourceEditionIdAndSha256Checksum(edition.editionId(), req.sha256Checksum().trim())
                .orElseGet(() -> {
                    SourceFileEntity f = new SourceFileEntity();
                    f.setSourceEditionId(edition.editionId());
                    f.setFileRole(req.fileRole().trim().toUpperCase());
                    f.setStorageProvider(req.storageProvider().trim().toUpperCase());
                    f.setStorageKey(req.storageKey().trim());
                    f.setMimeType(req.mimeType().trim().toLowerCase());
                    f.setFileSizeBytes(req.fileSizeBytes());
                    f.setSha256Checksum(req.sha256Checksum().trim());
                    f.setPageCount(req.pageCount());
                    return fileRepository.save(f);
                });

        boolean activeExists = jobRepository.existsBySourceEditionIdAndSourceFileIdAndJobStatusIn(
                edition.editionId(), file.getId(), List.of("QUEUED", "PROCESSING")
        );
        if (activeExists) {
            throw new IllegalArgumentException("An active ingestion job already exists for this file");
        }

        IngestionJobEntity job = new IngestionJobEntity();
        job.setSourceEditionId(edition.editionId());
        job.setSourceFileId(file.getId());
        job.setJobStatus("QUEUED");
        job.setRequestedBy(actor.userId());
        job = jobRepository.save(job);

        auditLogService.record(actor.userId(), actor.primaryRole(), "INGESTION_JOB_QUEUED",
                "INGESTION_JOB", job.getPublicId(), Map.of("mimeType", file.getMimeType()));

        return new IngestionJobView(job.getPublicId(), file.getPublicId(),
                job.getJobStatus(), job.getAttemptCount(), job.getErrorMessage());
    }

    @Transactional(readOnly = true)
    public Page<IngestionJobView> listJobs(Pageable pageable) {
        return jobRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(j -> new IngestionJobView(j.getPublicId(), null, j.getJobStatus(),
                        j.getAttemptCount(), j.getErrorMessage()));
    }
}

