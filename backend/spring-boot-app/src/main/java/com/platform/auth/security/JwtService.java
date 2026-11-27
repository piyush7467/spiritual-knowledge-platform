package com.platform.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;

    public JwtService(
            @Value("${platform.security.jwt-secret}") String jwtSecret,
            @Value("${platform.security.access-token-ttl-seconds:900}") long accessTtlSeconds,
            @Value("${platform.security.refresh-token-ttl-seconds:604800}") long refreshTtlSeconds
    ) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 bytes");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
    }

    public String generateAccessToken(PlatformUserPrincipal principal) {
        return buildToken(principal, "access", accessTtlSeconds);
    }

    public String generateRefreshToken(PlatformUserPrincipal principal) {
        return buildToken(principal, "refresh", refreshTtlSeconds);
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @SuppressWarnings("unchecked")
    public PlatformUserPrincipal toPrincipal(Claims claims) {
        Long userId = claims.get("uid", Long.class);
        UUID publicId = UUID.fromString(claims.getSubject());
        String email = claims.get("email", String.class);
        List<String> roles = claims.get("roles", List.class);
        return new PlatformUserPrincipal(userId, publicId, email, roles != null ? roles : List.of());
    }

    private String buildToken(PlatformUserPrincipal principal, String tokenType, long ttlSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(principal.publicId().toString())
                .claim("uid", principal.userId())
                .claim("email", principal.email())
                .claim("roles", principal.roles())
                .claim("typ", tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(signingKey)
                .compact();
    }
}

