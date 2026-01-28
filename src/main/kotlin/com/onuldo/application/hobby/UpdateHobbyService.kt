package com.onuldo.application.hobby

import com.onuldo.common.exception.DuplicateResourceException
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.hobby.Hobby
import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.model.UpdateHobbyCommand
import com.onuldo.port.inbound.hobby.usecase.UpdateHobbyUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateHobbyService(
    private val hobbyRepository: HobbyRepository
) : UpdateHobbyUseCase {

    @Transactional
    override fun execute(command: UpdateHobbyCommand): HobbyResponse {
        val hobby = hobbyRepository.findById(command.id)
            ?: throw ResourceNotFoundException("취미", command.id)

        command.name?.let { newName ->
            if (newName != hobby.name && hobbyRepository.existsByName(newName)) {
                throw DuplicateResourceException("취미", "이미 존재하는 취미입니다: $newName")
            }
        }

        val updatedHobby = Hobby(
            name = command.name ?: hobby.name,
            description = command.description ?: hobby.description,
            iconUrl = command.iconUrl ?: hobby.iconUrl,
            colorCode = command.colorCode ?: hobby.colorCode,
            isActive = hobby.isActive
        ).apply {
            this.id = hobby.id
        }

        val savedHobby = hobbyRepository.save(updatedHobby)

        return HobbyResponse(
            id = savedHobby.id ?: throw IllegalStateException("취미 ID가 없습니다."),
            name = savedHobby.name,
            description = savedHobby.description,
            iconUrl = savedHobby.iconUrl,
            colorCode = savedHobby.colorCode
        )
    }
}
