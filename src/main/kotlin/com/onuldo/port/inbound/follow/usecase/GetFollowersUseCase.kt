package com.onuldo.port.inbound.follow.usecase

import com.onuldo.port.inbound.follow.model.FollowUserResponse

interface GetFollowersUseCase {
    fun execute(userId: Long, currentUserId: Long): List<FollowUserResponse>
}
