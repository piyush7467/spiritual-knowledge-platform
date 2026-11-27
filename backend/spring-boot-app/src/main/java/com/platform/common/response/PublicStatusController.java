package com.platform.common.response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class PublicStatusController {

    @GetMapping("/status")
    public ApiResponse<Map<String, String>> status() {
        return ApiResponse.ok(Map.of(
                "service", "spiritual-knowledge-platform",
                "status", "UP"
        ));
    }
}

