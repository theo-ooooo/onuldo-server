package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.UserResponse

interface GetMyInfoUseCase {
    fun execute(userId: Long): UserResponse
}
