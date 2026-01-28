package com.onuldo.port.inbound.timer.usecase

import com.onuldo.port.inbound.timer.model.StartTimerCommand
import com.onuldo.port.inbound.timer.model.TimerResponse

interface StartTimerUseCase {
    fun execute(command: StartTimerCommand): TimerResponse
}
