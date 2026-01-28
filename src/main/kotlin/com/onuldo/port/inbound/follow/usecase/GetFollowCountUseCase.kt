package com.onuldo.port.inbound.follow.usecase

import com.onuldo.port.inbound.follow.model.FollowCountResponse

interface GetFollowCountUseCase {
    fun execute(userId: Long): FollowCountResponse
}
