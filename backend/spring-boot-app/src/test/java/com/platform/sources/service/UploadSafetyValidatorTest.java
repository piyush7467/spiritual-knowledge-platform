package com.platform.sources.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UploadSafetyValidatorTest {

    private final UploadSafetyValidator validator = new UploadSafetyValidator();

    @Test
    void acceptsValidPdfUnderLimit() {
        assertDoesNotThrow(() -> validator.validateFileMetadata("application/pdf", 1024 * 1024));
    }

    @Test
    void rejectsExecutableAndOversizedFiles() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateFileMetadata("application/x-msdownload", 1024));
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateFileMetadata("application/pdf", 30L * 1024L * 1024L));
    }

    @Test
    void blocksLocalhostAndPrivateNetworkSsrfUrls() {
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateExternalSourceUrl("http://localhost:8080/internal"));
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateExternalSourceUrl("http://127.0.0.1/admin"));
        assertDoesNotThrow(() -> validator.validateExternalSourceUrl("https://93.184.216.34/scripture.pdf"));
    }
}
