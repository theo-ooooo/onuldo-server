package com.onuldo.adapter.outbound.persistence.user

import com.onuldo.domain.user.User
import com.onuldo.port.outbound.user.UserRepository
import com.onuldo.port.outbound.user.UserWithFollowCountResult
import org.springframework.stereotype.Repository

/**
 * UserRepository 포트 구현체
 */
@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val userQueryRepository: UserQueryRepository
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
        userQueryRepository.searchUsersWithFollowCount(keyword).map { dto ->
            UserWithFollowCountResult(
                userId = dto.userId,
                email = dto.email,
                nickname = dto.nickname,
                profileImageUrl = dto.profileImageUrl,
                bio = dto.bio,
                followerCount = dto.followerCount,
                followingCount = dto.followingCount
            )
        }

    override fun findUserWithFollowCount(userId: Long): UserWithFollowCountResult? =
        userQueryRepository.findUserWithFollowCount(userId)?.let { dto ->
            UserWithFollowCountResult(
                userId = dto.userId,
                email = dto.email,
                nickname = dto.nickname,
                profileImageUrl = dto.profileImageUrl,
                bio = dto.bio,
                followerCount = dto.followerCount,
                followingCount = dto.followingCount
            )
        }
}

