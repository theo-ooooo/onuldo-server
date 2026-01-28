package com.onuldo.port.inbound.timer.usecase

import com.onuldo.port.inbound.timer.model.TimerResponse

interface GetCurrentTimerUseCase {
    fun execute(userId: Long): TimerResponse?
}
