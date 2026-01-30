package com.onuldo.application.hobby

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.DuplicateResourceException
import com.onuldo.common.exception.ErrorCode
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

        return HobbyResponse(
            id = hobby.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "취미 ID가 없습니다."),
            userId = hobby.userId,
            name = hobby.name,
            description = hobby.description,
            iconUrl = hobby.iconUrl,
            colorCode = hobby.colorCode
        )
    }
}
