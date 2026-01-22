package com.onuldo.application.hobby

import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.usecase.GetHobbyListUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetHobbyListService(
    private val hobbyRepository: HobbyRepository
) : GetHobbyListUseCase {

    @Transactional(readOnly = true)
    override fun execute(): List<HobbyResponse> {
        return hobbyRepository.findAllActive().map { hobby ->
            HobbyResponse(
                id = hobby.id ?: throw IllegalStateException("취미 ID가 없습니다."),
                name = hobby.name,
                description = hobby.description,
                iconUrl = hobby.iconUrl,
                colorCode = hobby.colorCode
            )
        }
    }
}

