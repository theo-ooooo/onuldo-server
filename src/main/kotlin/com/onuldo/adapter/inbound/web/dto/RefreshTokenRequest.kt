package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.NotBlank

/**
 * 토큰 재발급 요청 DTO (Web 계층)
 */
data class RefreshTokenRequest(
    @field:NotBlank
    val refreshToken: String
)

