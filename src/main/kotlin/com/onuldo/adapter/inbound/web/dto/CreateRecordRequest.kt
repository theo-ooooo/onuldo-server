package com.onuldo.adapter.inbound.web.dto

import com.onuldo.domain.record.RecordVisibility
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateRecordRequest(
    @field:NotNull(message = "취미 ID는 필수입니다.")
    val hobbyId: Long,

    val timerId: Long? = null,

    @field:NotNull(message = "활동 시간은 필수입니다.")
    @field:Min(value = 1, message = "활동 시간은 1초 이상이어야 합니다.")
    val durationSeconds: Int,

    val memo: String? = null,

    val visibility: RecordVisibility = RecordVisibility.PUBLIC,

    val tags: List<String>? = null,

    val activityDate: LocalDate? = null
)
