package com.onuldo.port.inbound.timer.model

import com.onuldo.domain.timer.TimerStatus
import java.time.LocalDateTime

data class TimerResponse(
    val id: Long,
    val hobbyId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val durationSeconds: Int,
    val status: TimerStatus
)
