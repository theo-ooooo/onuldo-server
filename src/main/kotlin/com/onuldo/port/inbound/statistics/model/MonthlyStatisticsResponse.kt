package com.onuldo.port.inbound.statistics.model

import java.time.YearMonth

data class MonthlyStatisticsResponse(
    val yearMonth: YearMonth,
    val totalDurationSeconds: Int,
    val recordCount: Int,
    val dailyStatistics: List<DailyStatisticsResponse>
)

