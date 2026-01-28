package com.onuldo.application.follow

import com.onuldo.port.inbound.follow.model.FollowCountResponse
import com.onuldo.port.inbound.follow.usecase.GetFollowCountUseCase
import com.onuldo.port.outbound.follow.FollowRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetFollowCountService(
    private val followRepository: FollowRepository
) : GetFollowCountUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): FollowCountResponse {
        val followingCount = followRepository.countByFollowerId(userId)
        val followerCount = followRepository.countByFollowingId(userId)

        return FollowCountResponse(
            followingCount = followingCount,
            followerCount = followerCount
        )
    }
}
