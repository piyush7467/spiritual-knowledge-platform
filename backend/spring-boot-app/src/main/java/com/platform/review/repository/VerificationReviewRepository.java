package com.platform.review.repository;

import com.platform.review.entity.VerificationReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VerificationReviewRepository extends JpaRepository<VerificationReviewEntity, Long> {
    List<VerificationReviewEntity> findAllByEntityTypeAndEntityPublicIdOrderByReviewedAtDesc(
            String entityType, UUID entityPublicId
    );
}

