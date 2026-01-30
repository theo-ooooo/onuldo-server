package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.UserResponse

interface GetUserUseCase {
    fun execute(userId: Long): UserResponse
}
