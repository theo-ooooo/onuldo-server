package com.onuldo.application.follow

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.follow.Follow
import com.onuldo.port.inbound.follow.usecase.FollowUserUseCase
import com.onuldo.port.outbound.follow.FollowRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FollowUserService(
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository
) : FollowUserUseCase {

    @Transactional
    override fun execute(followerId: Long, followingId: Long) {
        if (followerId == followingId) {
            throw CustomException(ErrorCode.FOLLOW_SELF_NOT_ALLOWED)
        }

        userRepository.findById(followingId)
            ?: throw ResourceNotFoundException("사용자", followingId)

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw CustomException(ErrorCode.FOLLOW_ALREADY_EXISTS)
        }

        val follow = Follow(
            followerId = followerId,
            followingId = followingId
        )

        followRepository.save(follow)
    }
}
