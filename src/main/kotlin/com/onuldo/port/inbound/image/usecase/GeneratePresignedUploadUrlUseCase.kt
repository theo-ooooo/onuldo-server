package com.onuldo.port.inbound.image.usecase

import com.onuldo.port.inbound.image.model.PresignedUploadUrlResponse

interface GeneratePresignedUploadUrlUseCase {
    fun execute(userId: Long, recordId: Long, fileName: String, contentType: String): PresignedUploadUrlResponse
}

