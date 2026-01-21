package com.onuldo.port.inbound.user.model

/**
 * 회원가입 유스케이스 입력 모델 (Command)
 *
 * Web 계층의 SignUpRequest와는 분리된, 유스케이스 관점의 입력 데이터입니다.
 */
data class RegisterUserCommand(
    val email: String,
    val rawPassword: String,
    val nickname: String,
    val profileImageUrl: String? = null,
    val bio: String? = null
)

