package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.ConfirmProfileImageUploadCommand
import com.onuldo.port.inbound.user.model.ProfileImageUploadResponse

/**
 * 프로필 이미지 업로드 확인 UseCase
 */
interface ConfirmProfileImageUploadUseCase {
    fun execute(command: ConfirmProfileImageUploadCommand): ProfileImageUploadResponse
}

