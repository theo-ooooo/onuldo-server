package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.CreateHobbyRequest
import com.onuldo.adapter.inbound.web.dto.UpdateHobbyRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.port.inbound.hobby.model.CreateHobbyCommand
import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.model.UpdateHobbyCommand
import com.onuldo.port.inbound.hobby.usecase.CreateHobbyUseCase
import com.onuldo.port.inbound.hobby.usecase.DeleteHobbyUseCase
import com.onuldo.port.inbound.hobby.usecase.GetHobbyListUseCase
import com.onuldo.port.inbound.hobby.usecase.UpdateHobbyUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Hobby", description = "취미 API")
@RestController
@RequestMapping("/api/hobbies")
class HobbyController(
    private val getHobbyListUseCase: GetHobbyListUseCase,
    private val createHobbyUseCase: CreateHobbyUseCase,
    private val updateHobbyUseCase: UpdateHobbyUseCase,
    private val deleteHobbyUseCase: DeleteHobbyUseCase
) {

    @Operation(summary = "취미 목록 조회", description = "활성화된 모든 취미 목록을 조회합니다.")
    @GetMapping
    fun getHobbyList(): ApiResponse<List<HobbyResponse>> {
        val hobbies = getHobbyListUseCase.execute()
        return ApiResponse.success(hobbies, message = "취미 목록을 조회했습니다.")
    }

    @Operation(summary = "취미 생성", description = "새로운 취미를 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createHobby(@Valid @RequestBody request: CreateHobbyRequest): ApiResponse<HobbyResponse> {
        val command = CreateHobbyCommand(
            name = request.name,
            description = request.description,
            iconUrl = request.iconUrl,
            colorCode = request.colorCode
        )
        val hobby = createHobbyUseCase.execute(command)
        return ApiResponse.success(hobby, message = "취미를 생성했습니다.")
    }

    @Operation(summary = "취미 수정", description = "기존 취미 정보를 수정합니다.")
    @PutMapping("/{id}")
    fun updateHobby(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateHobbyRequest
    ): ApiResponse<HobbyResponse> {
        val command = UpdateHobbyCommand(
            id = id,
            name = request.name,
            description = request.description,
            iconUrl = request.iconUrl,
            colorCode = request.colorCode
        )
        val hobby = updateHobbyUseCase.execute(command)
        return ApiResponse.success(hobby, message = "취미를 수정했습니다.")
    }

    @Operation(summary = "취미 삭제", description = "취미를 비활성화합니다.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteHobby(@PathVariable id: Long): ApiResponse<Unit> {
        deleteHobbyUseCase.execute(id)
        return ApiResponse.success(message = "취미를 삭제했습니다.")
    }
}
