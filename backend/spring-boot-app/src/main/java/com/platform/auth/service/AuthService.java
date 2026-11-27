package com.platform.auth.service;

import com.platform.auth.dto.AuthDtos.*;
import com.platform.auth.security.JwtService;
import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.audit.AuditLogService;
import com.platform.users.entity.RoleEntity;
import com.platform.users.entity.UserEntity;
import com.platform.users.repository.RoleRepository;
import com.platform.users.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuditLogService auditLogService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        RoleEntity userRole = roleRepository.findByCode("USER")
                .orElseThrow(() -> new IllegalStateException("Base USER role not seeded"));

        UserEntity user = new UserEntity();
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName().trim());
        user.setPreferredLanguage("hi".equalsIgnoreCase(request.preferredLanguage()) ? "hi" : "en");
        user.getRoles().add(userRole);
        user = userRepository.save(user);

        auditLogService.record(user.getId(), "USER", "USER_REGISTERED", "USER", user.getPublicId(),
                Map.of("preferredLanguage", user.getPreferredLanguage()));
        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        List<String> roles = user.getRoles().stream().map(RoleEntity::getCode).toList();
        String primaryRole = roles.isEmpty() ? "USER" : roles.getFirst();
        auditLogService.record(user.getId(), primaryRole, "USER_LOGIN", "USER", user.getPublicId(), Map.of());
        return buildAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshRequest request) {
        Claims claims = jwtService.parseToken(request.refreshToken());
        if (!"refresh".equals(claims.get("typ", String.class))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        PlatformUserPrincipal principal = jwtService.toPrincipal(claims);
        UserEntity user = userRepository.findById(principal.userId())
                .filter(UserEntity::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Account inactive or unavailable"));
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(UserEntity user) {
        List<String> roles = user.getRoles().stream().map(RoleEntity::getCode).sorted().toList();
        PlatformUserPrincipal principal = new PlatformUserPrincipal(
                user.getId(), user.getPublicId(), user.getEmail(), roles
        );
        return new AuthResponse(
                jwtService.generateAccessToken(principal),
                jwtService.generateRefreshToken(principal),
                user.getPublicId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getPreferredLanguage(),
                roles
        );
    }
}

