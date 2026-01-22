package com.onuldo.adapter.outbound.persistence.hobby

import com.onuldo.domain.hobby.Hobby
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HobbyJpaRepository : JpaRepository<Hobby, Long> {
    fun findByName(name: String): Hobby?
    fun findByIsActiveTrue(): List<Hobby>
    fun existsByName(name: String): Boolean
}

