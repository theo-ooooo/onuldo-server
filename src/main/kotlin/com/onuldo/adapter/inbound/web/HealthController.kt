package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Health", description = "헬스 체크 API")
@RestController
@RequestMapping("/api/health")
class HealthController {

    @Operation(summary = "헬스 체크", description = "서버 상태를 확인합니다")
    @GetMapping
    fun health(): ApiResponse<Map<String, String>> {
        return ApiResponse.success(
            data = mapOf("status" to "OK", "message" to "서버가 정상적으로 동작 중입니다.")
        )
    }
}

