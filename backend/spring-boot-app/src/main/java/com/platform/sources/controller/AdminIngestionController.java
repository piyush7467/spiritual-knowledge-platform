package com.platform.sources.controller;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.response.ApiResponse;
import com.platform.sources.service.CloudinarySignatureService;
import com.platform.sources.service.CloudinarySignatureService.SignedUploadParams;
import com.platform.sources.service.IngestionService;
import com.platform.sources.service.IngestionService.IngestionJobView;
import com.platform.sources.service.IngestionService.RegisterFileAndQueueRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/ingestion")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
public class AdminIngestionController {

    private final CloudinarySignatureService cloudinarySignatureService;
    private final IngestionService ingestionService;

    public AdminIngestionController(
            CloudinarySignatureService cloudinarySignatureService,
            IngestionService ingestionService
    ) {
        this.cloudinarySignatureService = cloudinarySignatureService;
        this.ingestionService = ingestionService;
    }

    @PostMapping("/upload-signature")
    public ApiResponse<SignedUploadParams> signUpload(@RequestParam(defaultValue = "sources") String folder) {
        return ApiResponse.ok(cloudinarySignatureService.createSignedUpload(folder));
    }

    @GetMapping("/jobs")
    public ApiResponse<Page<IngestionJobView>> listJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(ingestionService.listJobs(PageRequest.of(Math.max(page, 0), Math.min(size, 100))));
    }

    @PostMapping("/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<IngestionJobView> queueJob(
            @Valid @RequestBody RegisterFileAndQueueRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(ingestionService.registerFileAndQueueJob(request, actor));
    }
}

