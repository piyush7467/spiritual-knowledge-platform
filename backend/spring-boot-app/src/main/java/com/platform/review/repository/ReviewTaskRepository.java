package com.platform.review.repository;

import com.platform.review.entity.ReviewTaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewTaskRepository extends JpaRepository<ReviewTaskEntity, Long> {
    Page<ReviewTaskEntity> findAllByTaskStatusOrderByPriorityAscCreatedAtAsc(String taskStatus, Pageable pageable);
    Optional<ReviewTaskEntity> findFirstByEntityTypeAndEntityPublicIdAndTaskStatus(
            String entityType, UUID entityPublicId, String taskStatus
    );
}

