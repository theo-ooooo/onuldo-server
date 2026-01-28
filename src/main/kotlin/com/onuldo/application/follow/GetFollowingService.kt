package com.onuldo.application.follow

import com.onuldo.port.inbound.follow.model.FollowUserResponse
import com.onuldo.port.inbound.follow.usecase.GetFollowingUseCase
import com.onuldo.port.outbound.follow.FollowRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetFollowingService(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
) : GetFollowingUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, currentUserId: Long): List<FollowUserResponse> {
        val followingIds = followRepository.findFollowingIdsByFollowerId(userId)
        val currentUserFollowingIds = followRepository.findFollowingIdsByFollowerId(currentUserId).toSet()

        return followingIds.mapNotNull { followingId ->
            val user = userRepository.findById(followingId)
            user?.let {
                FollowUserResponse(
                    userId = it.id!!,
                    nickname = it.nickname,
                    profileImageUrl = it.profileImageUrl,
                    isFollowing = followingId in currentUserFollowingIds
                )
            }
        }
    }
}
