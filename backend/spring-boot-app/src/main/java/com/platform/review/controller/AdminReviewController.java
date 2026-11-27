package com.platform.review.controller;

import com.platform.auth.security.PlatformUserPrincipal;
import com.platform.common.response.ApiResponse;
import com.platform.review.entity.VerificationReviewEntity;
import com.platform.review.service.ReviewService;
import com.platform.review.service.ReviewService.ReviewDecisionRequest;
import com.platform.review.service.ReviewService.ReviewTaskView;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/review")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'REVIEWER')")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/queue")
    public ApiResponse<Page<ReviewTaskView>> listQueue(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(reviewService.listOpenTasks(PageRequest.of(Math.max(page, 0), Math.min(size, 100))));
    }

    @GetMapping("/{entityType}/{publicId}/history")
    public ApiResponse<List<VerificationReviewEntity>> history(
            @PathVariable String entityType,
            @PathVariable UUID publicId
    ) {
        return ApiResponse.ok(reviewService.getHistory(entityType, publicId));
    }

    @PostMapping("/{entityType}/{publicId}/decision")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<VerificationReviewEntity> decide(
            @PathVariable String entityType,
            @PathVariable UUID publicId,
            @Valid @RequestBody ReviewDecisionRequest request,
            @AuthenticationPrincipal PlatformUserPrincipal actor
    ) {
        return ApiResponse.ok(reviewService.submitDecision(entityType, publicId, request, actor));
    }
}

