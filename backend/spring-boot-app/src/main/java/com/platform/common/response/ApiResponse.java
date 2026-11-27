package com.platform.common.response;

import java.time.Instant;
import java.util.List;

/**
 * Unified API envelope for all /api/v1 endpoints.
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        ErrorDetail error,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> fail(String code, String message, List<String> fieldErrors) {
        return new ApiResponse<>(false, null, new ErrorDetail(code, message, fieldErrors), Instant.now());
    }

    public record ErrorDetail(String code, String message, List<String> fieldErrors) {
    }
}

