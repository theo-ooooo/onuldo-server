package com.onuldo.port.inbound.user.model

/**
 * 프로필 이미지 업로드 확인 Command
 */
data class ConfirmProfileImageUploadCommand(
    val userId: Long,
    val imageKey: String,
    val fileName: String,
    val fileSize: Long,
    val width: Int? = null,
    val height: Int? = null
)

