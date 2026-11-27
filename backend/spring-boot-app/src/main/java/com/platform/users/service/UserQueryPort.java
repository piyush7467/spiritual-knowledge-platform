package com.platform.users.service;

import java.util.List;
import java.util.UUID;

/**
 * Public cross-module query interface for user lookups.
 */
public interface UserQueryPort {
    UserSummary findByEmail(String email);
    UserSummary findById(Long userId);

    record UserSummary(Long id, UUID publicId, String email, String displayName, String preferredLanguage, List<String> roles) {
    }
}

