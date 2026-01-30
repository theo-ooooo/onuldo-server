package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.CreateHobbyRequest
import com.onuldo.adapter.inbound.web.dto.UpdateHobbyRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.hobby.model.CreateHobbyCommand
import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.model.UpdateHobbyCommand
import com.onuldo.port.inbound.hobby.usecase.CreateHobbyUseCase
import com.onuldo.port.inbound.hobby.usecase.DeleteHobbyUseCase
import com.onuldo.port.inbound.hobby.usecase.GetMyHobbiesUseCase
import com.onuldo.port.inbound.hobby.usecase.UpdateHobbyUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Hobby", description = "취미 API")
@RestController
@RequestMapping("/api/hobbies")
class HobbyController(
    private val securityUtils: SecurityUtils,
    private val createHobbyUseCase: CreateHobbyUseCase,
    private val getMyHobbiesUseCase: GetMyHobbiesUseCase,
    private val updateHobbyUseCase: UpdateHobbyUseCase,
    private val deleteHobbyUseCase: DeleteHobbyUseCase
) {

    @Operation(summary = "취미 생성", description = "새로운 취미를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createHobby(
        request: HttpServletRequest,
        @Valid @RequestBody body: CreateHobbyRequest
    ): ApiResponse<HobbyResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = CreateHobbyCommand(
            name = body.name,
            description = body.description,
            iconUrl = body.iconUrl,
            colorCode = body.colorCode
        )
        val hobby = createHobbyUseCase.execute(userId, command)
        return ApiResponse.success(hobby, message = "취미를 생성했습니다.")
    }

    @Operation(summary = "내 취미 목록 조회", description = "현재 로그인한 사용자의 취미 목록을 조회합니다.")
    @GetMapping
    fun getMyHobbies(request: HttpServletRequest): ApiResponse<List<HobbyResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val hobbies = getMyHobbiesUseCase.execute(userId)
        return ApiResponse.success(hobbies, message = "취미 목록을 조회했습니다.")
    }

    @Operation(summary = "취미 수정", description = "기존 취미를 수정합니다.")
    @PutMapping("/{hobbyId}")
    fun updateHobby(
        request: HttpServletRequest,
        @PathVariable hobbyId: Long,
        @Valid @RequestBody body: UpdateHobbyRequest
    ): ApiResponse<HobbyResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = UpdateHobbyCommand(
            id = hobbyId,
            name = body.name,
            description = body.description,
            iconUrl = body.iconUrl,
            colorCode = body.colorCode
        )
        val hobby = updateHobbyUseCase.execute(userId, command)
        return ApiResponse.success(hobby, message = "취미를 수정했습니다.")
    }

    @Operation(summary = "취미 삭제", description = "기존 취미를 삭제합니다.")
    @DeleteMapping("/{hobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteHobby(
        request: HttpServletRequest,
        @PathVariable hobbyId: Long
    ): ApiResponse<Unit> {
        val userId = securityUtils.getCurrentUserId(request)
        deleteHobbyUseCase.execute(hobbyId, userId)
        return ApiResponse.success(message = "취미를 삭제했습니다.")
    }
}

