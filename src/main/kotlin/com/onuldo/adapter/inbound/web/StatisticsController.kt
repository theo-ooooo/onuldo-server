package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.service.StatisticsService
import com.onuldo.common.service.UserStatistics
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Statistics", description = "통계 API")
@RestController
@RequestMapping("/api/statistics")
class StatisticsController(
    private val statisticsService: StatisticsService
) {

    @Operation(summary = "사용자 통계 조회", description = "일/주/월 통계 및 취미별 통계를 조회합니다.")
    @GetMapping("/me")
    fun getMyStatistics(authentication: Authentication): ApiResponse<UserStatistics> {
        // JWT에서 userId 추출 (현재는 임시로 구현)
        val userId = getUserIdFromAuthentication(authentication)
        
        // 코루틴을 사용한 비동기 통계 계산
        val statistics = runBlocking {
            statisticsService.calculateUserStatistics(userId)
        }
        
        return ApiResponse.success(statistics, message = "통계를 조회했습니다.")
    }

    private fun getUserIdFromAuthentication(authentication: Authentication): Long {
        // TODO: 실제 구현 시 JWT에서 userId 추출
        // 현재는 임시 구현
        return 1L
    }
}

