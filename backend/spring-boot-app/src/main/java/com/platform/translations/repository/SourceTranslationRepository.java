package com.platform.translations.repository;

import com.platform.common.audit.EvidenceStatus;
import com.platform.translations.entity.SourceTranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SourceTranslationRepository extends JpaRepository<SourceTranslationEntity, Long> {
    Optional<SourceTranslationEntity> findByPublicId(UUID publicId);
    List<SourceTranslationEntity> findAllByOriginalPassageIdAndTranslationStatus(Long passageId, EvidenceStatus status);
    List<SourceTranslationEntity> findAllByOriginalPassageId(Long passageId);
}

