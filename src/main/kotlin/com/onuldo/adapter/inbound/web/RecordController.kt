package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.CreateRecordRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.record.model.CreateRecordCommand
import com.onuldo.port.inbound.record.model.RecordResponse
import com.onuldo.port.inbound.record.usecase.CreateRecordUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Tag(name = "Record", description = "기록 API")
@RestController
@RequestMapping("/api/records")
class RecordController(
    private val securityUtils: SecurityUtils,
    private val createRecordUseCase: CreateRecordUseCase
) {

    @Operation(summary = "기록 생성", description = "새로운 활동 기록을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createRecord(
        request: HttpServletRequest,
        @Valid @RequestBody body: CreateRecordRequest
    ): ApiResponse<RecordResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = CreateRecordCommand(
            userId = userId,
            hobbyId = body.hobbyId,
            timerId = body.timerId,
            durationSeconds = body.durationSeconds,
            memo = body.memo,
            visibility = body.visibility,
            tagNames = body.tags ?: emptyList(),
            activityDate = body.activityDate ?: LocalDate.now()
        )
        val record = createRecordUseCase.execute(command)
        return ApiResponse.success(record, message = "기록을 생성했습니다.")
    }
}
