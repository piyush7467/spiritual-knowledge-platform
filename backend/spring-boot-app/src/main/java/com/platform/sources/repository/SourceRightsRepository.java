package com.platform.sources.repository;

import com.platform.sources.entity.SourceRightsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRightsRepository extends JpaRepository<SourceRightsEntity, Long> {
    Optional<SourceRightsEntity> findFirstBySourceId(Long sourceId);
}

