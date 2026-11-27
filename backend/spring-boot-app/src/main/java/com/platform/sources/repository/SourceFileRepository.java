package com.platform.sources.repository;

import com.platform.sources.entity.SourceFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourceFileRepository extends JpaRepository<SourceFileEntity, Long> {
    Optional<SourceFileEntity> findByPublicId(UUID publicId);
    Optional<SourceFileEntity> findFirstBySourceEditionIdAndSha256Checksum(Long editionId, String sha256Checksum);
}

