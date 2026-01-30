package com.onuldo.application.hobby

import com.onuldo.common.exception.DuplicateResourceException
import com.onuldo.common.exception.ResourceNotFoundException
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
    override fun execute(userId: Long, command: UpdateHobbyCommand): HobbyResponse {
        val hobby = hobbyRepository.findByIdAndUserId(command.id, userId)
            ?: throw ResourceNotFoundException("취미", command.id)

        command.name?.let { newName ->
            if (newName != hobby.name && hobbyRepository.existsByNameAndUserIdAndIdNot(newName, userId, command.id)) {
                throw DuplicateResourceException("취미", "이미 존재하는 취미입니다: $newName")
            }
        }

        hobby.update(
            name = command.name,
            description = command.description,
            iconUrl = command.iconUrl,
            colorCode = command.colorCode
        )

        val savedHobby = hobbyRepository.save(hobby)

        return HobbyResponse(
            id = savedHobby.id ?: throw IllegalStateException("취미 ID가 없습니다."),
            userId = savedHobby.userId,
            name = savedHobby.name,
            description = savedHobby.description,
            iconUrl = savedHobby.iconUrl,
            colorCode = savedHobby.colorCode
        )
    }
}
