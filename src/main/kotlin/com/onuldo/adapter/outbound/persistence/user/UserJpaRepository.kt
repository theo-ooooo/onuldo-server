package com.onuldo.adapter.outbound.persistence.user

import com.onuldo.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserJpaRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    
    @Query("SELECT u FROM User u WHERE u.nickname LIKE %:keyword% OR u.email LIKE %:keyword%")
    fun searchByKeyword(@Param("keyword") keyword: String): List<User>
}


