package com.onuldo.port.inbound.image.usecase

import com.onuldo.port.inbound.image.model.ImageUploadResponse

interface ConfirmImageUploadUseCase {
    fun execute(command: com.onuldo.port.inbound.image.model.ConfirmImageUploadCommand): ImageUploadResponse
}


