package com.platform.sources.controller;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.response.ApiResponse;
import com.platform.sources.dto.SourceDtos.*;
import com.platform.sources.service.SourceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/sources")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'REVIEWER')")
public class AdminSourceController {

    private final SourceService sourceService;

    public AdminSourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping
    public ApiResponse<Page<SourceView>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(sourceService.listAllForAdmin(PageRequest.of(Math.max(page, 0), Math.min(size, 100))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SourceView> create(
            @Valid @RequestBody CreateSourceRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(sourceService.createSource(request, actor));
    }

    @PostMapping("/{publicId}/editions")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EditionView> addEdition(
            @PathVariable UUID publicId,
            @Valid @RequestBody CreateEditionRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(sourceService.addEdition(publicId, request, actor));
    }

    @PutMapping("/{publicId}/rights")
    public ApiResponse<RightsView> upsertRights(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpsertRightsRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(sourceService.upsertRights(publicId, request, actor));
    }

    @PostMapping("/{publicId}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<SourceView> transitionStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody TransitionStatusRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(sourceService.transitionStatus(publicId, request.targetStatus(), actor));
    }
}

