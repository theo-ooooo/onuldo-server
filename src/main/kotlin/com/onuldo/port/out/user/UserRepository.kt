package com.onuldo.port.out.user

import com.onuldo.domain.user.User

/**
 * 사용자 리포지토리 포트 (Outbound Port)
 *
 * 인프라 계층(JPA 등)에서 이 인터페이스를 구현합니다.
 */
interface UserRepository {

    fun save(user: User): User

    fun findById(id: Long): User?

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}


