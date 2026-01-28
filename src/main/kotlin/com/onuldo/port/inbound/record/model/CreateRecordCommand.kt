package com.onuldo.port.inbound.record.model

import com.onuldo.domain.record.RecordVisibility
import java.time.LocalDate

data class CreateRecordCommand(
    val userId: Long,
    val hobbyId: Long,
    val timerId: Long? = null,
    val durationSeconds: Int,
    val memo: String? = null,
    val visibility: RecordVisibility = RecordVisibility.PUBLIC,
    val tagNames: List<String> = emptyList(),
    val activityDate: LocalDate = LocalDate.now()
)
