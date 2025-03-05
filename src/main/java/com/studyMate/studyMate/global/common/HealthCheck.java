package com.studyMate.studyMate.global.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Common")
public class HealthCheck {
    @GetMapping("health")
    @Operation(summary = "Health Check", description = "헬스체크 API")
    public String health(){
        return "ok";
    }
}
