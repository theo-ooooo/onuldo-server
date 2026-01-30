package com.onuldo.adapter.outbound.persistence.hobby

import com.onuldo.domain.hobby.Hobby
import org.springframework.data.jpa.repository.JpaRepository

interface HobbyJpaRepository : JpaRepository<Hobby, Long> {
    fun findByUserId(userId: Long): List<Hobby>
    fun findByUserIdAndIsActive(userId: Long, isActive: Boolean): List<Hobby>
    fun findByIdAndUserId(id: Long, userId: Long): Hobby?
    fun existsByNameAndUserId(name: String, userId: Long): Boolean
    fun existsByNameAndUserIdAndIdNot(name: String, userId: Long, excludeId: Long): Boolean
}

