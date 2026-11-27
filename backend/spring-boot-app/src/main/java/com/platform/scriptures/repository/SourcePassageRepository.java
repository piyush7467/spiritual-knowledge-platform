package com.platform.scriptures.repository;

import com.platform.common.audit.EvidenceStatus;
import com.platform.scriptures.entity.SourcePassageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourcePassageRepository extends JpaRepository<SourcePassageEntity, Long> {
    Optional<SourcePassageEntity> findByPublicId(UUID publicId);
    Optional<SourcePassageEntity> findByPublicIdAndVerificationStatus(UUID publicId, EvidenceStatus status);
    Page<SourcePassageEntity> findAllByVerificationStatus(EvidenceStatus status, Pageable pageable);
}

