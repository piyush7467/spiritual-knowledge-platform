package com.platform.common.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void okWrapsPayloadWithTimestamp() {
        ApiResponse<String> response = ApiResponse.ok("verified");
        assertTrue(response.success());
        assertEquals("verified", response.data());
        assertNull(response.error());
        assertNotNull(response.timestamp());
    }

    @Test
    void failWrapsErrorWithoutData() {
        ApiResponse<Void> response = ApiResponse.fail("NOT_FOUND", "Missing passage", List.of("publicId"));
        assertFalse(response.success());
        assertNull(response.data());
        assertNotNull(response.error());
        assertEquals("NOT_FOUND", response.error().code());
        assertEquals(List.of("publicId"), response.error().fieldErrors());
    }
}

