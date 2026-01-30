package com.onuldo.port.inbound.statistics.model

import java.time.LocalDate

data class StreakResponse(
    val currentStreak: Int,
    val longestStreak: Int,
    val lastRecordDate: LocalDate?
)

