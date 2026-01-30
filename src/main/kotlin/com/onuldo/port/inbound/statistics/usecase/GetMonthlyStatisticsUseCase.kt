package com.onuldo.port.inbound.statistics.usecase

import com.onuldo.port.inbound.statistics.model.MonthlyStatisticsResponse
import java.time.YearMonth

interface GetMonthlyStatisticsUseCase {
    fun execute(userId: Long, yearMonth: YearMonth): MonthlyStatisticsResponse
}

