package com.onuldo.port.inbound.hobby.usecase

import com.onuldo.port.inbound.hobby.model.HobbyResponse
import com.onuldo.port.inbound.hobby.model.UpdateHobbyCommand

interface UpdateHobbyUseCase {
    fun execute(command: UpdateHobbyCommand): HobbyResponse
}
