package com.onuldo.port.inbound.statistics.usecase

import com.onuldo.port.inbound.statistics.model.HobbyStatisticsResponse

interface GetHobbyStatisticsUseCase {
    fun execute(userId: Long): List<HobbyStatisticsResponse>
}

