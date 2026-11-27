package com.platform.scriptures.repository;

import com.platform.scriptures.entity.SourceReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceReferenceRepository extends JpaRepository<SourceReferenceEntity, Long> {
    List<SourceReferenceEntity> findAllByPassageId(Long passageId);
    List<SourceReferenceEntity> findAllByCanonicalRefKeyIgnoreCase(String canonicalRefKey);
}

