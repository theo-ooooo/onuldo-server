package com.onuldo.application.statistics

import com.onuldo.port.inbound.statistics.model.WeeklyStatisticsResponse
import com.onuldo.port.inbound.statistics.usecase.GetWeeklyStatisticsUseCase
import com.onuldo.port.inbound.statistics.usecase.GetDailyStatisticsUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class GetWeeklyStatisticsService(
    private val getDailyStatisticsUseCase: GetDailyStatisticsUseCase
) : GetWeeklyStatisticsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, weekStartDate: LocalDate): WeeklyStatisticsResponse {
        // Adjust to Monday if needed
        val monday = weekStartDate.with(DayOfWeek.MONDAY)
        val sunday = monday.plusDays(6)
        
        val dailyStatistics = getDailyStatisticsUseCase.execute(userId, monday, sunday)
        
        val totalDurationSeconds = dailyStatistics.sumOf { it.totalDurationSeconds }
        val recordCount = dailyStatistics.sumOf { it.recordCount }
        
        return WeeklyStatisticsResponse(
            weekStartDate = monday,
            weekEndDate = sunday,
            totalDurationSeconds = totalDurationSeconds,
            recordCount = recordCount,
            dailyStatistics = dailyStatistics
        )
    }
}

