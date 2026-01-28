package com.onuldo.application.follow

import com.onuldo.port.inbound.follow.model.FollowUserResponse
import com.onuldo.port.inbound.follow.usecase.GetFollowersUseCase
import com.onuldo.port.outbound.follow.FollowRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetFollowersService(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
) : GetFollowersUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, currentUserId: Long): List<FollowUserResponse> {
        val followerIds = followRepository.findFollowerIdsByFollowingId(userId)
        val currentUserFollowingIds = followRepository.findFollowingIdsByFollowerId(currentUserId).toSet()

        return followerIds.mapNotNull { followerId ->
            val user = userRepository.findById(followerId)
            user?.let {
                FollowUserResponse(
                    userId = it.id!!,
                    nickname = it.nickname,
                    profileImageUrl = it.profileImageUrl,
                    isFollowing = followerId in currentUserFollowingIds
                )
            }
        }
    }
}
