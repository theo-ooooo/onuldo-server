package com.onuldo.application.hobby

import com.onuldo.common.exception.DuplicateResourceException
import com.onuldo.domain.hobby.Hobby
import com.onuldo.port.inbound.hobby.model.CreateHobbyCommand
import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.usecase.CreateHobbyUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateHobbyService(
    private val hobbyRepository: HobbyRepository
) : CreateHobbyUseCase {

    @Transactional
    override fun execute(command: CreateHobbyCommand): HobbyResponse {
        if (hobbyRepository.existsByName(command.name)) {
            throw DuplicateResourceException("취미", "이미 존재하는 취미입니다: ${command.name}")
        }

        val hobby = Hobby(
            name = command.name,
            description = command.description,
            iconUrl = command.iconUrl,
            colorCode = command.colorCode
        )

        val savedHobby = hobbyRepository.save(hobby)

        return HobbyResponse(
            id = savedHobby.id ?: throw IllegalStateException("취미 ID가 없습니다."),
            name = savedHobby.name,
            description = savedHobby.description,
            iconUrl = savedHobby.iconUrl,
            colorCode = savedHobby.colorCode
        )
    }
}
