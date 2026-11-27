package com.platform.translations.controller;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.response.ApiResponse;
import com.platform.translations.service.TranslationQueryPort.TranslationSummary;
import com.platform.translations.service.TranslationService;
import com.platform.translations.service.TranslationService.CreateTranslationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/translations")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'REVIEWER')")
public class AdminTranslationController {

    private final TranslationService translationService;

    public AdminTranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TranslationSummary> create(
            @Valid @RequestBody CreateTranslationRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(translationService.createTranslation(request, actor));
    }

    @PostMapping("/{publicId}/publish")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<TranslationSummary> publish(
            @PathVariable UUID publicId,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(translationService.publishTranslation(publicId, actor));
    }
}

