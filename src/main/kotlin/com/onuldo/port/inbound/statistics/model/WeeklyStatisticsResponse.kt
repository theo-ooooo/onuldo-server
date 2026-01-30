package com.onuldo.port.inbound.statistics.model

import java.time.LocalDate

data class WeeklyStatisticsResponse(
    val weekStartDate: LocalDate,
    val weekEndDate: LocalDate,
    val totalDurationSeconds: Int,
    val recordCount: Int,
    val dailyStatistics: List<DailyStatisticsResponse>
)

