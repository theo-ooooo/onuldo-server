package com.onuldo.adapter.outbound.persistence.hobby

import com.onuldo.domain.hobby.Hobby
import com.onuldo.port.outbound.hobby.HobbyRepository
import org.springframework.stereotype.Repository

@Repository
class HobbyRepositoryImpl(
    private val jpaRepository: HobbyJpaRepository
) : HobbyRepository {

    override fun save(hobby: Hobby): Hobby {
        return jpaRepository.save(hobby)
    }

    override fun findById(id: Long): Hobby? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findByIdAndUserId(id: Long, userId: Long): Hobby? {
        return jpaRepository.findByIdAndUserId(id, userId)
    }

    override fun findByUserId(userId: Long): List<Hobby> {
        return jpaRepository.findByUserId(userId)
    }

    override fun findByUserIdAndIsActive(userId: Long, isActive: Boolean): List<Hobby> {
        return jpaRepository.findByUserIdAndIsActive(userId, isActive)
    }

    override fun existsByNameAndUserId(name: String, userId: Long): Boolean {
        return jpaRepository.existsByNameAndUserId(name, userId)
    }

    override fun existsByNameAndUserIdAndIdNot(name: String, userId: Long, excludeId: Long): Boolean {
        return jpaRepository.existsByNameAndUserIdAndIdNot(name, userId, excludeId)
    }
}

