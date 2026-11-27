package com.platform.users.service;

import com.platform.users.entity.UserEntity;
import com.platform.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserQueryService implements UserQueryPort {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserSummary findByEmail(String email) {
        UserEntity user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toSummary(user);
    }

    @Override
    public UserSummary findById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toSummary(user);
    }

    private UserSummary toSummary(UserEntity user) {
        return new UserSummary(
                user.getId(),
                user.getPublicId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getPreferredLanguage(),
                user.getRoles().stream().map(r -> r.getCode()).toList()
        );
    }
}

