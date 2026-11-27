package com.platform.sources.service;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * Enforces file upload MIME/size restrictions and SSRF URL checks.
 */
@Component
public class UploadSafetyValidator {

    public static final long MAX_FILE_BYTES = 25L * 1024L * 1024L;
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "image/webp"
    );

    public void validateFileMetadata(String mimeType, long fileSizeBytes) {
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.trim().toLowerCase())) {
            throw new IllegalArgumentException("Unsupported MIME type. Allowed: PDF, PNG, JPEG, WEBP.");
        }
        if (fileSizeBytes <= 0 || fileSizeBytes > MAX_FILE_BYTES) {
            throw new IllegalArgumentException("File size must be between 1 byte and 25 MB.");
        }
    }

    public void validateExternalSourceUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            return;
        }
        URI uri = URI.create(rawUrl.trim());
        String scheme = uri.getScheme();
        if (scheme == null || (!scheme.equalsIgnoreCase("https") && !scheme.equalsIgnoreCase("http"))) {
            throw new IllegalArgumentException("Source URL must use http or https");
        }
        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("Source URL must contain a valid host");
        }
        if (host.equalsIgnoreCase("localhost") || host.endsWith(".local")) {
            throw new IllegalArgumentException("Localhost/internal source URLs are blocked");
        }
        try {
            InetAddress addr = InetAddress.getByName(host);
            if (addr.isAnyLocalAddress() || addr.isLoopbackAddress()
                    || addr.isLinkLocalAddress() || addr.isSiteLocalAddress()) {
                throw new IllegalArgumentException("Private or loopback network hosts are blocked");
            }
        } catch (UnknownHostException ignored) {
            // Allow unresolvable hostnames in offline validation unless explicitly private IP literal
        }
    }
}

