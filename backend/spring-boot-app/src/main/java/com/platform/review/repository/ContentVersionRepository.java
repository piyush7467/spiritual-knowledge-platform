package com.platform.review.repository;

import com.platform.review.entity.ContentVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContentVersionRepository extends JpaRepository<ContentVersionEntity, Long> {
    Optional<ContentVersionEntity> findTopByEntityTypeAndEntityPublicIdOrderByVersionNumberDesc(
            String entityType, UUID entityPublicId
    );
}

