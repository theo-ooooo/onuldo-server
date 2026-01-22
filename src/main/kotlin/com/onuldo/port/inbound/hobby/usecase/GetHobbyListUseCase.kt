package com.onuldo.port.inbound.hobby.usecase

import com.onuldo.port.inbound.hobby.model.HobbyResponse

/**
 * 취미 목록 조회 UseCase
 */
interface GetHobbyListUseCase {
    fun execute(): List<HobbyResponse>
}

