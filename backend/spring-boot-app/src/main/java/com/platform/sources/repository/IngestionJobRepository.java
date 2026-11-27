package com.platform.sources.repository;

import com.platform.sources.entity.IngestionJobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IngestionJobRepository extends JpaRepository<IngestionJobEntity, Long> {
    Optional<IngestionJobEntity> findByPublicId(UUID publicId);
    boolean existsBySourceEditionIdAndSourceFileIdAndJobStatusIn(Long editionId, Long fileId, java.util.Collection<String> statuses);
    Page<IngestionJobEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

