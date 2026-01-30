package com.onuldo.application.hobby

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.usecase.GetMyHobbiesUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyHobbiesService(
    private val hobbyRepository: HobbyRepository
) : GetMyHobbiesUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): List<HobbyResponse> {
        val hobbies = hobbyRepository.findByUserIdAndIsActive(userId, isActive = true)
        return hobbies.map { hobby ->
            HobbyResponse(
                id = hobby.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "취미 ID가 없습니다."),
                userId = hobby.userId,
                name = hobby.name,
                description = hobby.description,
                iconUrl = hobby.iconUrl,
                colorCode = hobby.colorCode
            )
        }
    }
}

