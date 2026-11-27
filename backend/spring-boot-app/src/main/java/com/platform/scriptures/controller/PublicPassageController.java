package com.platform.scriptures.controller;

import com.platform.common.response.ApiResponse;
import com.platform.scriptures.dto.ScriptureDtos.PassageView;
import com.platform.scriptures.service.ScriptureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/passages")
public class PublicPassageController {

    private final ScriptureService scriptureService;

    public PublicPassageController(ScriptureService scriptureService) {
        this.scriptureService = scriptureService;
    }

    @GetMapping
    public ApiResponse<Page<PassageView>> listPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(scriptureService.listPublished(PageRequest.of(Math.max(page, 0), Math.min(size, 50))));
    }

    @GetMapping("/{publicId}")
    public ApiResponse<PassageView> getPublished(@PathVariable UUID publicId) {
        return ApiResponse.ok(scriptureService.getPublished(publicId));
    }
}

