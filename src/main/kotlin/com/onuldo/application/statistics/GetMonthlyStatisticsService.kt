package com.onuldo.application.statistics

import com.onuldo.port.inbound.statistics.model.MonthlyStatisticsResponse
import com.onuldo.port.inbound.statistics.usecase.GetMonthlyStatisticsUseCase
import com.onuldo.port.inbound.statistics.usecase.GetDailyStatisticsUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.YearMonth

@Service
class GetMonthlyStatisticsService(
    private val getDailyStatisticsUseCase: GetDailyStatisticsUseCase
) : GetMonthlyStatisticsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, yearMonth: YearMonth): MonthlyStatisticsResponse {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        
        val dailyStatistics = getDailyStatisticsUseCase.execute(userId, startDate, endDate)
        
        val totalDurationSeconds = dailyStatistics.sumOf { it.totalDurationSeconds }
        val recordCount = dailyStatistics.sumOf { it.recordCount }
        
        return MonthlyStatisticsResponse(
            yearMonth = yearMonth,
            totalDurationSeconds = totalDurationSeconds,
            recordCount = recordCount,
            dailyStatistics = dailyStatistics
        )
    }
}

