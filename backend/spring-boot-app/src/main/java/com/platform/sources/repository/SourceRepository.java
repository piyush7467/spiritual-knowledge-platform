package com.platform.sources.repository;

import com.platform.common.audit.EvidenceStatus;
import com.platform.sources.entity.SourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourceRepository extends JpaRepository<SourceEntity, Long> {
    Optional<SourceEntity> findByPublicId(UUID publicId);
    Optional<SourceEntity> findByPublicIdAndVerificationStatus(UUID publicId, EvidenceStatus status);
    Page<SourceEntity> findAllByVerificationStatus(EvidenceStatus status, Pageable pageable);
}

