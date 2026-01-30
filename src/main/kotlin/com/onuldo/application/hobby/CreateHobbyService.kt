package com.onuldo.application.hobby

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.DuplicateResourceException
import com.onuldo.common.exception.ErrorCode
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
    override fun execute(userId: Long, command: CreateHobbyCommand): HobbyResponse {
        if (hobbyRepository.existsByNameAndUserId(command.name, userId)) {
            throw DuplicateResourceException("취미", "이미 존재하는 취미입니다: ${command.name}")
        }

        val hobby = Hobby(
            userId = userId,
            name = command.name,
            description = command.description,
            iconUrl = command.iconUrl,
            colorCode = command.colorCode
        )

        val savedHobby = hobbyRepository.save(hobby)

        return HobbyResponse(
            id = savedHobby.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "취미 ID가 없습니다."),
            userId = savedHobby.userId,
            name = savedHobby.name,
            description = savedHobby.description,
            iconUrl = savedHobby.iconUrl,
            colorCode = savedHobby.colorCode
        )
    }
}
