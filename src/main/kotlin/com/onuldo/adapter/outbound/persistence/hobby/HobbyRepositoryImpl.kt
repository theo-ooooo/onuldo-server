package com.onuldo.adapter.outbound.persistence.hobby

import com.onuldo.domain.hobby.Hobby
import com.onuldo.port.outbound.hobby.HobbyRepository
import org.springframework.stereotype.Repository

@Repository
class HobbyRepositoryImpl(
    private val hobbyJpaRepository: HobbyJpaRepository
) : HobbyRepository {

    override fun save(hobby: Hobby): Hobby = hobbyJpaRepository.save(hobby)

    override fun findById(id: Long): Hobby? =
        hobbyJpaRepository.findById(id).orElse(null)

    override fun findByName(name: String): Hobby? =
        hobbyJpaRepository.findByName(name)

    override fun findAllActive(): List<Hobby> =
        hobbyJpaRepository.findByIsActiveTrue()

    override fun existsByName(name: String): Boolean =
        hobbyJpaRepository.existsByName(name)
}

