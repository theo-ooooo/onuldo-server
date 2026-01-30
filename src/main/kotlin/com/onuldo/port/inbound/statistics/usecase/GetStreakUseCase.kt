package com.onuldo.port.inbound.statistics.usecase

import com.onuldo.port.inbound.statistics.model.StreakResponse

interface GetStreakUseCase {
    fun execute(userId: Long): StreakResponse
}

