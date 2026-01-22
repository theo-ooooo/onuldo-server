package com.onuldo.adapter.outbound.persistence.user

import com.onuldo.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA 리포지토리
 */
@Repository
interface UserJpaRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
