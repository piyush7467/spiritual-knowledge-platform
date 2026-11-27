package com.platform.auth.security;

import java.util.List;
import java.util.UUID;

public record PlatformUserPrincipal(
        Long userId,
        UUID publicId,
        String email,
        List<String> roles
) {
    public String primaryRole() {
        return roles.isEmpty() ? "USER" : roles.getFirst();
    }
}

