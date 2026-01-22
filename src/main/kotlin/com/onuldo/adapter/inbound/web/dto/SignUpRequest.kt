package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * 회원가입 요청 DTO (Web 계층)
 */
data class SignUpRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    @field:Size(min = 8, max = 100)
    val password: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val nickname: String,

    val profileImageUrl: String? = null,
    val bio: String? = null
)

