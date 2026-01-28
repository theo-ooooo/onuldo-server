package com.onuldo.port.inbound.timer.usecase

import com.onuldo.port.inbound.timer.model.TimerResponse

interface PauseTimerUseCase {
    fun execute(userId: Long): TimerResponse
}
