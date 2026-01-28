package com.onuldo.port.inbound.timer.model

data class StartTimerCommand(
    val userId: Long,
    val hobbyId: Long
)
