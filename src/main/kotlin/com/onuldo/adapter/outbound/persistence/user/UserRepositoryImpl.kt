package com.onuldo.adapter.outbound.persistence.user

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.user.User
import com.onuldo.port.outbound.follow.FollowRepository
import com.onuldo.port.outbound.user.UserRepository
import com.onuldo.port.outbound.user.UserWithFollowCountResult
import org.springframework.stereotype.Repository

/**
 * UserRepository 포트 구현체
 */
@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val followRepository: FollowRepository
) : UserRepository {

    override fun save(user: User): User = userJpaRepository.save(user)

    override fun findById(id: Long): User? =
        userJpaRepository.findById(id).orElse(null)

    override fun findByEmail(email: String): User? =
        userJpaRepository.findByEmail(email)

    override fun existsByEmail(email: String): Boolean =
        userJpaRepository.existsByEmail(email)

    override fun existsByNickname(nickname: String): Boolean =
        userJpaRepository.existsByNickname(nickname)

    override fun searchByKeyword(keyword: String): List<User> =
        userJpaRepository.searchByKeyword(keyword)

    override fun searchUsersWithFollowCount(keyword: String): List<UserWithFollowCountResult> =
        searchByKeyword(keyword).map { user ->
            UserWithFollowCountResult(
                userId = user.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "사용자 ID가 없습니다."),
                email = user.email,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl,
                bio = user.bio,
                followerCount = followRepository.countByFollowingId(user.id!!),
                followingCount = followRepository.countByFollowerId(user.id!!),
                fcmToken = user.fcmToken,
            )
        }

    override fun findUserWithFollowCount(userId: Long): UserWithFollowCountResult? =
        findById(userId)?.let { user ->
            UserWithFollowCountResult(
                userId = user.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "사용자 ID가 없습니다."),
                email = user.email,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl,
                bio = user.bio,
                followerCount = followRepository.countByFollowingId(userId),
                followingCount = followRepository.countByFollowerId(userId),
                fcmToken = user.fcmToken,
            )
        }
}

