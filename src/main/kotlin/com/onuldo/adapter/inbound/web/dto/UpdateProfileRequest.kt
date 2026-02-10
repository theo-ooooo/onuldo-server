package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.Size

/**
 * 프로필 수정 요청 DTO
 */
data class UpdateProfileRequest(
    @field:Size(max = 50, message = "닉네임은 50자 이하여야 합니다.")
    val nickname: String? = null,

    @field:Size(max = 255, message = "소개는 255자 이하여야 합니다.")
    val bio: String? = null
)

