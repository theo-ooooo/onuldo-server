package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.StartTimerRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.timer.model.StartTimerCommand
import com.onuldo.port.inbound.timer.model.TimerResponse
import com.onuldo.port.inbound.timer.usecase.GetCurrentTimerUseCase
import com.onuldo.port.inbound.timer.usecase.PauseTimerUseCase
import com.onuldo.port.inbound.timer.usecase.ResumeTimerUseCase
import com.onuldo.port.inbound.timer.usecase.StartTimerUseCase
import com.onuldo.port.inbound.timer.usecase.StopTimerUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Timer", description = "타이머 API")
@RestController
@RequestMapping("/api/timers")
class TimerController(
    private val securityUtils: SecurityUtils,
    private val startTimerUseCase: StartTimerUseCase,
    private val pauseTimerUseCase: PauseTimerUseCase,
    private val resumeTimerUseCase: ResumeTimerUseCase,
    private val stopTimerUseCase: StopTimerUseCase,
    private val getCurrentTimerUseCase: GetCurrentTimerUseCase
) {

    @Operation(summary = "타이머 시작", description = "새로운 타이머를 시작합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun startTimer(
        request: HttpServletRequest,
        @Valid @RequestBody body: StartTimerRequest
    ): ApiResponse<TimerResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = StartTimerCommand(
            userId = userId,
            hobbyId = body.hobbyId
        )
        val timer = startTimerUseCase.execute(command)
        return ApiResponse.success(timer, message = "타이머를 시작했습니다.")
    }

    @Operation(summary = "타이머 일시정지", description = "실행 중인 타이머를 일시정지합니다.")
    @PostMapping("/pause")
    fun pauseTimer(request: HttpServletRequest): ApiResponse<TimerResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val timer = pauseTimerUseCase.execute(userId)
        return ApiResponse.success(timer, message = "타이머를 일시정지했습니다.")
    }

    @Operation(summary = "타이머 재개", description = "일시정지된 타이머를 재개합니다.")
    @PostMapping("/resume")
    fun resumeTimer(request: HttpServletRequest): ApiResponse<TimerResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val timer = resumeTimerUseCase.execute(userId)
        return ApiResponse.success(timer, message = "타이머를 재개했습니다.")
    }

    @Operation(summary = "타이머 종료", description = "실행 중인 타이머를 종료합니다.")
    @PostMapping("/stop")
    fun stopTimer(request: HttpServletRequest): ApiResponse<TimerResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val timer = stopTimerUseCase.execute(userId)
        return ApiResponse.success(timer, message = "타이머를 종료했습니다.")
    }

    @Operation(summary = "현재 타이머 조회", description = "현재 실행 중인 타이머를 조회합니다.")
    @GetMapping("/current")
    fun getCurrentTimer(request: HttpServletRequest): ApiResponse<TimerResponse?> {
        val userId = securityUtils.getCurrentUserId(request)
        val timer = getCurrentTimerUseCase.execute(userId)
        return if (timer != null) {
            ApiResponse.success(timer, message = "현재 타이머를 조회했습니다.")
        } else {
            ApiResponse.success(null, message = "실행 중인 타이머가 없습니다.")
        }
    }
}
