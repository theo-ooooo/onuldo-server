package com.onuldo.application.user

import com.onuldo.common.config.S3Properties
import com.onuldo.port.inbound.user.model.UserResponse
import com.onuldo.port.inbound.user.usecase.SearchUsersUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SearchUsersService(
    private val userRepository: UserRepository,
    private val s3Properties: S3Properties
) : SearchUsersUseCase {

    @Transactional(readOnly = true)
    override fun execute(keyword: String, currentUserId: Long): List<UserResponse> {
        return userRepository.searchUsersWithFollowCount(keyword)
            .filter { it.userId != currentUserId }
            .map { result ->
                // 프로필 이미지 URL에 S3 도메인 추가
                val profileImageUrl = result.profileImageUrl?.let { imageKey ->
                    "${s3Properties.baseUrl}/$imageKey"
                }

                UserResponse(
                    userId = result.userId,
                    email = result.email,
                    nickname = result.nickname,
                    profileImageUrl = profileImageUrl,
                    bio = result.bio,
                    followerCount = result.followerCount,
                    followingCount = result.followingCount,
                    fcmToken = result.fcmToken,
                )
            }
    }
}
