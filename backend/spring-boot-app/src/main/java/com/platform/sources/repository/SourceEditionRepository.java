package com.platform.sources.repository;

import com.platform.sources.entity.SourceEditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SourceEditionRepository extends JpaRepository<SourceEditionEntity, Long> {
    Optional<SourceEditionEntity> findByPublicId(UUID publicId);
    List<SourceEditionEntity> findAllBySourceId(Long sourceId);
}

