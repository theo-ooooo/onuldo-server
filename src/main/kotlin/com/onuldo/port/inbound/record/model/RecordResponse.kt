package com.onuldo.port.inbound.record.model

import com.onuldo.domain.record.RecordVisibility
import java.time.LocalDate
import java.time.LocalDateTime

data class RecordResponse(
    val id: Long,
    val userId: Long,
    val hobbyId: Long,
    val timerId: Long?,
    val durationSeconds: Int,
    val memo: String?,
    val visibility: RecordVisibility,
    val activityDate: LocalDate,
    val tags: List<String>,
    val createdAt: LocalDateTime
)
