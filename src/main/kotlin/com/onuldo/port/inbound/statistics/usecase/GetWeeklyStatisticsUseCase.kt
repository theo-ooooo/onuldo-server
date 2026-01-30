package com.onuldo.port.inbound.statistics.usecase

import com.onuldo.port.inbound.statistics.model.WeeklyStatisticsResponse
import java.time.LocalDate

interface GetWeeklyStatisticsUseCase {
    fun execute(userId: Long, weekStartDate: LocalDate): WeeklyStatisticsResponse
}

