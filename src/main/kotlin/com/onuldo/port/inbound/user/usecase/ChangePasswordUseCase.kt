package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.ChangePasswordCommand

/**
 * 비밀번호 변경 UseCase
 */
interface ChangePasswordUseCase {
    fun execute(command: ChangePasswordCommand)
}

