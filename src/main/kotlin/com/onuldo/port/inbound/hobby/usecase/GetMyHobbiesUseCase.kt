package com.onuldo.port.inbound.hobby.usecase

import com.onuldo.port.inbound.hobby.model.HobbyResponse

interface GetMyHobbiesUseCase {
    fun execute(userId: Long): List<HobbyResponse>
}

