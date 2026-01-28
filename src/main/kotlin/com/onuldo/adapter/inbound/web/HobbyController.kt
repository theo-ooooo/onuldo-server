package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.usecase.GetHobbyListUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Hobby", description = "취미 API")
@RestController
@RequestMapping("/api/hobbies")
class HobbyController(
    private val getHobbyListUseCase: GetHobbyListUseCase
) {

    @Operation(summary = "취미 목록 조회", description = "활성화된 모든 취미 목록을 조회합니다.")
    @GetMapping
    fun getHobbyList(): ApiResponse<List<HobbyResponse>> {
        val hobbies = getHobbyListUseCase.execute()
        return ApiResponse.success(hobbies, message = "취미 목록을 조회했습니다.")
    }
}

