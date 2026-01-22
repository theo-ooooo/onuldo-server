package com.onuldo.adapter.outbound.persistence.user

import com.onuldo.domain.user.User
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Repository

/**
 * UserRepository 포트 구현체
 */
@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun save(user: User): User = userJpaRepository.save(user)

    override fun findById(id: Long): User? =
        userJpaRepository.findById(id).orElse(null)

    override fun findByEmail(email: String): User? =
        userJpaRepository.findByEmail(email)

    override fun existsByEmail(email: String): Boolean =
        userJpaRepository.existsByEmail(email)
}

