package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.NotNull

data class StartTimerRequest(
    @field:NotNull(message = "취미 ID는 필수입니다.")
    val hobbyId: Long
)
