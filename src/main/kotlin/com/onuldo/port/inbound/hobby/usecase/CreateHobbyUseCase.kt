package com.onuldo.port.inbound.hobby.usecase

import com.onuldo.port.inbound.hobby.model.CreateHobbyCommand
import com.onuldo.port.inbound.hobby.model.HobbyResponse

interface CreateHobbyUseCase {
    fun execute(command: CreateHobbyCommand): HobbyResponse
}
