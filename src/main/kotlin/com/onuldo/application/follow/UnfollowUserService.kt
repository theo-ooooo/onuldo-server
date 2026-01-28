package com.onuldo.application.follow

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.port.inbound.follow.usecase.UnfollowUserUseCase
import com.onuldo.port.outbound.follow.FollowRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UnfollowUserService(
    private val followRepository: FollowRepository
) : UnfollowUserUseCase {

    @Transactional
    override fun execute(followerId: Long, followingId: Long) {
        val follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
            ?: throw CustomException(ErrorCode.FOLLOW_NOT_FOUND)

        followRepository.delete(follow)
    }
}
