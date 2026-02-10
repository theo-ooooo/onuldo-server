package com.onuldo.port.inbound.user.model

/**
 * 비밀번호 변경 유스케이스 입력 모델 (Command)
 */
data class ChangePasswordCommand(
    val userId: Long,
    val currentPassword: String,
    val newPassword: String
)

