package com.onuldo.application.statistics

import com.onuldo.port.inbound.statistics.model.HobbyStatisticsResponse
import com.onuldo.port.inbound.statistics.usecase.GetHobbyStatisticsUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import com.onuldo.port.outbound.userhobby.UserHobbyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetHobbyStatisticsService(
    private val userHobbyRepository: UserHobbyRepository,
    private val hobbyRepository: HobbyRepository
) : GetHobbyStatisticsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): List<HobbyStatisticsResponse> {
        val userHobbies = userHobbyRepository.findByUserId(userId)
        
        return userHobbies.map { userHobby ->
            val hobby = hobbyRepository.findById(userHobby.hobbyId)
                ?: throw com.onuldo.common.exception.ResourceNotFoundException("취미", userHobby.hobbyId)
            
            HobbyStatisticsResponse(
                hobbyId = userHobby.hobbyId,
                hobbyName = hobby.name,
                totalDurationSeconds = userHobby.totalDurationSeconds,
                recordCount = userHobby.recordCount,
                firstRecordedAt = userHobby.firstRecordedAt,
                lastRecordedAt = userHobby.lastRecordedAt
            )
        }.sortedByDescending { it.totalDurationSeconds }
    }
}

