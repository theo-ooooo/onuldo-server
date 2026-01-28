package com.onuldo.port.inbound.follow.usecase

interface UnfollowUserUseCase {
    fun execute(followerId: Long, followingId: Long)
}
