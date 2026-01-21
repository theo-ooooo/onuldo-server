package com.onuldo.port.inbound.user.model

/**
 * 로그인 유스케이스 결과 모델 (Result)
 *
 * 토큰 정보까지 포함한 도메인/애플리케이션 계층용 결과입니다.
 * Web 계층에서는 이 모델을 그대로 응답에 씁니다.
 */
data class LoginResult(
    val userId: Long,
    val email: String,
    val nickname: String,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"
)


