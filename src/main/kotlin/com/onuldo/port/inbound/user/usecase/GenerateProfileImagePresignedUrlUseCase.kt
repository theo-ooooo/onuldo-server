package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.image.model.PresignedUploadUrlResponse

/**
 * 프로필 이미지 Presigned URL 생성 UseCase
 */
interface GenerateProfileImagePresignedUrlUseCase {
    fun execute(userId: Long, fileName: String, contentType: String): PresignedUploadUrlResponse
}

