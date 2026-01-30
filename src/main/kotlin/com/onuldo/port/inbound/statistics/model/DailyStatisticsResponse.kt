package com.onuldo.port.inbound.statistics.model

import java.time.LocalDate

data class DailyStatisticsResponse(
    val date: LocalDate,
    val totalDurationSeconds: Int,
    val recordCount: Int
)

