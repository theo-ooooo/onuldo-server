package com.onuldo.port.inbound.statistics.usecase

import com.onuldo.port.inbound.statistics.model.DailyStatisticsResponse
import java.time.LocalDate

interface GetDailyStatisticsUseCase {
    fun execute(userId: Long, startDate: LocalDate, endDate: LocalDate): List<DailyStatisticsResponse>
}

