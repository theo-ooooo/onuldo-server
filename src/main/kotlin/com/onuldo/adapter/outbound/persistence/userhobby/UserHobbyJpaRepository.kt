package com.onuldo.adapter.outbound.persistence.userhobby

import com.onuldo.domain.userhobby.UserHobby
import org.springframework.data.jpa.repository.JpaRepository

interface UserHobbyJpaRepository : JpaRepository<UserHobby, Long> {
    fun findByUserId(userId: Long): List<UserHobby>
    fun findByUserIdAndHobbyId(userId: Long, hobbyId: Long): UserHobby?
}
