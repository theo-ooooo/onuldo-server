package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.UpdateProfileCommand

/**
 * 프로필 수정 UseCase
 */
interface UpdateProfileUseCase {
    fun execute(command: UpdateProfileCommand)
}

