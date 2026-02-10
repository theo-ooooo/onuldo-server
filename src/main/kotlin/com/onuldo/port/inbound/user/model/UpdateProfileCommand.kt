package com.onuldo.port.inbound.user.model

/**
 * 프로필 수정 유스케이스 입력 모델 (Command)
 */
data class UpdateProfileCommand(
    val userId: Long,
    val nickname: String?,
    val bio: String?
)

