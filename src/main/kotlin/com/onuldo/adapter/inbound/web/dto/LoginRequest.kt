package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * 로그인 요청 DTO (Web 계층)
 */
data class LoginRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
)

