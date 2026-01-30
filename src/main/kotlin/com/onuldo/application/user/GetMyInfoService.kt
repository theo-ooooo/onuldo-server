package com.onuldo.application.user

import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.user.model.UserResponse
import com.onuldo.port.inbound.user.usecase.GetMyInfoUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyInfoService(
    private val userRepository: UserRepository
) : GetMyInfoUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): UserResponse {
        val result = userRepository.findUserWithFollowCount(userId)
            ?: throw ResourceNotFoundException("사용자", userId, ErrorCode.USER_NOT_FOUND)

        return UserResponse(
            userId = result.userId,
            email = result.email,
            nickname = result.nickname,
            profileImageUrl = result.profileImageUrl,
            bio = result.bio,
            followerCount = result.followerCount,
            followingCount = result.followingCount
        )
    }
}
