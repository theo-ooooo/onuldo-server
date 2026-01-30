package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.statistics.model.CalendarRecordResponse
import com.onuldo.port.inbound.statistics.model.DailyStatisticsResponse
import com.onuldo.port.inbound.statistics.model.HobbyStatisticsResponse
import com.onuldo.port.inbound.statistics.model.MonthlyStatisticsResponse
import com.onuldo.port.inbound.statistics.model.StreakResponse
import com.onuldo.port.inbound.statistics.model.WeeklyStatisticsResponse
import com.onuldo.port.inbound.statistics.usecase.GetCalendarRecordsUseCase
import com.onuldo.port.inbound.statistics.usecase.GetDailyStatisticsUseCase
import com.onuldo.port.inbound.statistics.usecase.GetHobbyStatisticsUseCase
import com.onuldo.port.inbound.statistics.usecase.GetMonthlyStatisticsUseCase
import com.onuldo.port.inbound.statistics.usecase.GetStreakUseCase
import com.onuldo.port.inbound.statistics.usecase.GetWeeklyStatisticsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.YearMonth

@Tag(name = "Statistics", description = "통계 API")
@RestController
@RequestMapping("/api/statistics")
class StatisticsController(
    private val securityUtils: SecurityUtils,
    private val getHobbyStatisticsUseCase: GetHobbyStatisticsUseCase,
    private val getDailyStatisticsUseCase: GetDailyStatisticsUseCase,
    private val getWeeklyStatisticsUseCase: GetWeeklyStatisticsUseCase,
    private val getMonthlyStatisticsUseCase: GetMonthlyStatisticsUseCase,
    private val getStreakUseCase: GetStreakUseCase,
    private val getCalendarRecordsUseCase: GetCalendarRecordsUseCase
) {

    @Operation(summary = "취미별 누적 시간 통계 조회", description = "사용자의 취미별 누적 시간 통계를 조회합니다.")
    @GetMapping("/hobbies")
    fun getHobbyStatistics(request: HttpServletRequest): ApiResponse<List<HobbyStatisticsResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val statistics = getHobbyStatisticsUseCase.execute(userId)
        return ApiResponse.success(statistics, message = "취미별 통계를 조회했습니다.")
    }

    @Operation(summary = "일별 통계 조회", description = "지정된 기간의 일별 활동 통계를 조회합니다.")
    @GetMapping("/daily")
    fun getDailyStatistics(
        request: HttpServletRequest,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ApiResponse<List<DailyStatisticsResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val statistics = getDailyStatisticsUseCase.execute(userId, startDate, endDate)
        return ApiResponse.success(statistics, message = "일별 통계를 조회했습니다.")
    }

    @Operation(summary = "주별 통계 조회", description = "지정된 주의 활동 통계를 조회합니다.")
    @GetMapping("/weekly")
    fun getWeeklyStatistics(
        request: HttpServletRequest,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) weekStartDate: LocalDate
    ): ApiResponse<WeeklyStatisticsResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val statistics = getWeeklyStatisticsUseCase.execute(userId, weekStartDate)
        return ApiResponse.success(statistics, message = "주별 통계를 조회했습니다.")
    }

    @Operation(summary = "월별 통계 조회", description = "지정된 월의 활동 통계를 조회합니다.")
    @GetMapping("/monthly")
    fun getMonthlyStatistics(
        request: HttpServletRequest,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") yearMonth: YearMonth
    ): ApiResponse<MonthlyStatisticsResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val statistics = getMonthlyStatisticsUseCase.execute(userId, yearMonth)
        return ApiResponse.success(statistics, message = "월별 통계를 조회했습니다.")
    }

    @Operation(summary = "연속 기록 스트릭 조회", description = "사용자의 연속 기록 스트릭을 조회합니다.")
    @GetMapping("/streak")
    fun getStreak(request: HttpServletRequest): ApiResponse<StreakResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val streak = getStreakUseCase.execute(userId)
        return ApiResponse.success(streak, message = "스트릭을 조회했습니다.")
    }

    @Operation(summary = "캘린더 기록 조회", description = "지정된 월의 캘린더 기록을 조회합니다.")
    @GetMapping("/calendar")
    fun getCalendarRecords(
        request: HttpServletRequest,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") yearMonth: YearMonth
    ): ApiResponse<List<CalendarRecordResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val records = getCalendarRecordsUseCase.execute(userId, yearMonth)
        return ApiResponse.success(records, message = "캘린더 기록을 조회했습니다.")
    }
}

