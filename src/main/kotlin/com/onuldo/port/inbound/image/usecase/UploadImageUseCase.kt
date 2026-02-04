package com.onuldo.port.inbound.image.usecase

import com.onuldo.port.inbound.image.model.ImageUploadResponse
import org.springframework.web.multipart.MultipartFile

interface UploadImageUseCase {
    fun execute(userId: Long, recordId: Long, file: MultipartFile): ImageUploadResponse
}


