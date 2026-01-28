package com.onuldo.application.hobby

import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.hobby.usecase.DeleteHobbyUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteHobbyService(
    private val hobbyRepository: HobbyRepository
) : DeleteHobbyUseCase {

    @Transactional
    override fun execute(id: Long) {
        val hobby = hobbyRepository.findById(id)
            ?: throw ResourceNotFoundException("취미", id)

        hobby.deactivate()
        hobbyRepository.save(hobby)
    }
}
