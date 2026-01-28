package com.onuldo.adapter.outbound.persistence.userhobby

import com.onuldo.domain.userhobby.UserHobby
import com.onuldo.port.outbound.userhobby.UserHobbyRepository
import org.springframework.stereotype.Repository

@Repository
class UserHobbyRepositoryImpl(
    private val userHobbyJpaRepository: UserHobbyJpaRepository
) : UserHobbyRepository {

    override fun save(userHobby: UserHobby): UserHobby {
        return userHobbyJpaRepository.save(userHobby)
    }

    override fun findByUserId(userId: Long): List<UserHobby> {
        return userHobbyJpaRepository.findByUserId(userId)
    }

    override fun findByUserIdAndHobbyId(userId: Long, hobbyId: Long): UserHobby? {
        return userHobbyJpaRepository.findByUserIdAndHobbyId(userId, hobbyId)
    }

    override fun findOrCreate(userId: Long, hobbyId: Long): UserHobby {
        return userHobbyJpaRepository.findByUserIdAndHobbyId(userId, hobbyId)
            ?: userHobbyJpaRepository.save(UserHobby(userId = userId, hobbyId = hobbyId))
    }
}
