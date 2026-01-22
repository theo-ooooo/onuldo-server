package com.onuldo.port.outbound.hobby

import com.onuldo.domain.hobby.Hobby

/**
 * 취미 리포지토리 포트
 */
interface HobbyRepository {
    fun save(hobby: Hobby): Hobby
    fun findById(id: Long): Hobby?
    fun findByName(name: String): Hobby?
    fun findAllActive(): List<Hobby>
    fun existsByName(name: String): Boolean
}

