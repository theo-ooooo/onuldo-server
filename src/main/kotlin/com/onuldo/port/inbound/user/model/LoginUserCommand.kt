package com.onuldo.port.inbound.user.model

/**
 * 로그인 유스케이스 입력 모델 (Command)
 *
 * Web 계층의 LoginRequest와는 분리된, 유스케이스 관점의 입력 데이터입니다.
 */
data class LoginUserCommand(
    val email: String,
    val rawPassword: String
)

