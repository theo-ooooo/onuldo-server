package com.onuldo.port.inbound.statistics.model

import java.time.LocalDate

data class CalendarRecordResponse(
    val date: LocalDate,
    val hasRecord: Boolean,
    val recordCount: Int = 0,
    val totalDurationSeconds: Int = 0
)

