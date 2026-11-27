package com.platform.scriptures.controller;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.response.ApiResponse;
import com.platform.scriptures.dto.ScriptureDtos.*;
import com.platform.scriptures.service.ScriptureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/passages")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'REVIEWER')")
public class AdminPassageController {

    private final ScriptureService scriptureService;

    public AdminPassageController(ScriptureService scriptureService) {
        this.scriptureService = scriptureService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PassageView> create(
            @Valid @RequestBody CreatePassageRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(scriptureService.createPassage(request, actor));
    }

    @PostMapping("/{publicId}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<PassageView> transitionStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody PassageStatusRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(scriptureService.transitionStatus(publicId, request.targetStatus(), actor));
    }
}

