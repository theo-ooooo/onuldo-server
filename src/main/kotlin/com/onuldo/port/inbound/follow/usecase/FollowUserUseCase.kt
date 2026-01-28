package com.onuldo.port.inbound.follow.usecase

interface FollowUserUseCase {
    fun execute(followerId: Long, followingId: Long)
}
