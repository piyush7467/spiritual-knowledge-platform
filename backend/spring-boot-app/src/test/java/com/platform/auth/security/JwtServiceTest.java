package com.platform.auth.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String TEST_SECRET = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";

    @Test
    void generatesAndParsesAccessToken() {
        JwtService jwtService = new JwtService(TEST_SECRET, 900, 604800);
        UUID userPublicId = UUID.randomUUID();
        PlatformUserPrincipal principal = new PlatformUserPrincipal(
                42L, userPublicId, "reviewer@example.org", List.of("SUPER_ADMIN")
        );

        String token = jwtService.generateAccessToken(principal);
        Claims claims = jwtService.parseToken(token);
        PlatformUserPrincipal parsed = jwtService.toPrincipal(claims);

        assertEquals("access", claims.get("typ", String.class));
        assertEquals(42L, parsed.userId());
        assertEquals(userPublicId, parsed.publicId());
        assertEquals("reviewer@example.org", parsed.email());
        assertEquals(List.of("SUPER_ADMIN"), parsed.roles());
    }

    @Test
    void rejectsShortJwtSecret() {
        assertThrows(IllegalStateException.class, () -> new JwtService("too-short", 900, 604800));
    }
}

