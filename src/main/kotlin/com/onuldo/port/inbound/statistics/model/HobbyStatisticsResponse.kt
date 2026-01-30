package com.onuldo.port.inbound.statistics.model

data class HobbyStatisticsResponse(
    val hobbyId: Long,
    val hobbyName: String,
    val totalDurationSeconds: Int,
    val recordCount: Int,
    val firstRecordedAt: java.time.LocalDateTime?,
    val lastRecordedAt: java.time.LocalDateTime?
)

