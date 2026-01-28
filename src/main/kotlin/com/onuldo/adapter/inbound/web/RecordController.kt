package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.CreateRecordRequest
import com.onuldo.adapter.inbound.web.dto.UpdateRecordRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.record.model.CreateRecordCommand
import com.onuldo.port.inbound.record.model.RecordResponse
import com.onuldo.port.inbound.record.model.UpdateRecordCommand
import com.onuldo.port.inbound.record.usecase.CreateRecordUseCase
import com.onuldo.port.inbound.record.usecase.DeleteRecordUseCase
import com.onuldo.port.inbound.record.usecase.GetMyRecordsUseCase
import com.onuldo.port.inbound.record.usecase.GetRecordUseCase
import com.onuldo.port.inbound.record.usecase.UpdateRecordUseCase
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
    private val createRecordUseCase: CreateRecordUseCase,
    private val getRecordUseCase: GetRecordUseCase,
    private val getMyRecordsUseCase: GetMyRecordsUseCase,
    private val updateRecordUseCase: UpdateRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase
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
            tagNames = body.tags,
            activityDate = body.activityDate ?: LocalDate.now()
        )
        val record = createRecordUseCase.execute(command)
        return ApiResponse.success(record, message = "기록을 생성했습니다.")
    }

    @Operation(summary = "내 기록 목록 조회", description = "내 기록 목록을 페이징하여 조회합니다.")
    @GetMapping
    fun getMyRecords(
        request: HttpServletRequest,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ApiResponse<List<RecordResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val records = getMyRecordsUseCase.execute(userId, page, size)
        return ApiResponse.success(records, message = "기록 목록을 조회했습니다.")
    }

    @Operation(summary = "기록 상세 조회", description = "기록의 상세 정보를 조회합니다.")
    @GetMapping("/{recordId}")
    fun getRecord(
        request: HttpServletRequest,
        @PathVariable recordId: Long
    ): ApiResponse<RecordResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val record = getRecordUseCase.execute(userId, recordId)
        return ApiResponse.success(record, message = "기록을 조회했습니다.")
    }

    @Operation(summary = "기록 수정", description = "기록의 메모와 공개 범위를 수정합니다.")
    @PutMapping("/{recordId}")
    fun updateRecord(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @Valid @RequestBody body: UpdateRecordRequest
    ): ApiResponse<RecordResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = UpdateRecordCommand(
            userId = userId,
            recordId = recordId,
            memo = body.memo,
            visibility = body.visibility
        )
        val record = updateRecordUseCase.execute(command)
        return ApiResponse.success(record, message = "기록을 수정했습니다.")
    }

    @Operation(summary = "기록 삭제", description = "기록을 삭제합니다.")
    @DeleteMapping("/{recordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecord(
        request: HttpServletRequest,
        @PathVariable recordId: Long
    ): ApiResponse<Unit> {
        val userId = securityUtils.getCurrentUserId(request)
        deleteRecordUseCase.execute(userId, recordId)
        return ApiResponse.success(message = "기록을 삭제했습니다.")
    }
}
