package com.onuldo.adapter.inbound.web.dto

/**
 * 회원가입 응답 DTO (Web 계층)
 */
data class SignUpResponse(
    val userId: Long,
    val email: String,
    val nickname: String
)

