package com.onuldo.port.outbound.hobby

import com.onuldo.domain.hobby.Hobby

interface HobbyRepository {
    fun save(hobby: Hobby): Hobby
    fun findById(id: Long): Hobby?
    fun findByIdAndUserId(id: Long, userId: Long): Hobby?
    fun findByUserId(userId: Long): List<Hobby>
    fun findByUserIdAndIsActive(userId: Long, isActive: Boolean): List<Hobby>
    fun existsByNameAndUserId(name: String, userId: Long): Boolean
    fun existsByNameAndUserIdAndIdNot(name: String, userId: Long, excludeId: Long): Boolean
}

