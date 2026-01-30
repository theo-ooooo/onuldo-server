package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.UserResponse

interface SearchUsersUseCase {
    fun execute(keyword: String, currentUserId: Long): List<UserResponse>
}
