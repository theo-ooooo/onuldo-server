package com.onuldo.port.outbound.userhobby

import com.onuldo.domain.userhobby.UserHobby

/**
 * 사용자-취미 리포지토리 포트
 */
interface UserHobbyRepository {
    fun save(userHobby: UserHobby): UserHobby
    fun findByUserId(userId: Long): List<UserHobby>
    fun findByUserIdAndHobbyId(userId: Long, hobbyId: Long): UserHobby?
    fun findOrCreate(userId: Long, hobbyId: Long): UserHobby
}

