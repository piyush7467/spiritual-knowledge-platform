package com.platform.sources.controller;

import com.platform.common.response.ApiResponse;
import com.platform.sources.dto.SourceDtos.SourceView;
import com.platform.sources.service.SourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/sources")
public class PublicSourceController {

    private final SourceService sourceService;

    public PublicSourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping
    public ApiResponse<Page<SourceView>> listPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(sourceService.listPublished(PageRequest.of(Math.max(page, 0), Math.min(size, 50))));
    }

    @GetMapping("/{publicId}")
    public ApiResponse<SourceView> getPublished(@PathVariable UUID publicId) {
        return ApiResponse.ok(sourceService.getPublished(publicId));
    }
}

