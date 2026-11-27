package com.platform.users.repository;

import com.platform.users.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findByPublicId(UUID publicId);
    boolean existsByEmailIgnoreCase(String email);
}

