package com.platform.sources.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

/**
 * Generates short-lived server-signed upload parameters for Cloudinary.
 * Never exposes CLOUDINARY_API_SECRET to the browser.
 */
@Service
public class CloudinarySignatureService {

    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;

    public CloudinarySignatureService(
            @Value("${platform.cloudinary.cloud-name:}") String cloudName,
            @Value("${platform.cloudinary.api-key:}") String apiKey,
            @Value("${platform.cloudinary.api-secret:}") String apiSecret
    ) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public record SignedUploadParams(
            String cloudName,
            String apiKey,
            long timestamp,
            String folder,
            String signature
    ) {}

    public SignedUploadParams createSignedUpload(String folder) {
        if (cloudName.isBlank() || apiKey.isBlank() || apiSecret.isBlank() || apiSecret.startsWith("CHANGE_ME")) {
            throw new IllegalStateException("Cloudinary credentials are not configured on the backend");
        }
        long timestamp = Instant.now().getEpochSecond();
        String safeFolder = folder.replaceAll("[^a-zA-Z0-9/_-]", "");
        String payloadToSign = "folder=" + safeFolder + "&timestamp=" + timestamp + apiSecret;
        String signature = sha1Hex(payloadToSign);
        return new SignedUploadParams(cloudName, apiKey, timestamp, safeFolder, signature);
    }

    private String sha1Hex(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-1 algorithm unavailable", ex);
        }
    }
}

