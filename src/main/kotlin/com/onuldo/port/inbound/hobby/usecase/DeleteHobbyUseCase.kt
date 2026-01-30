package com.onuldo.port.inbound.hobby.usecase

interface DeleteHobbyUseCase {
    fun execute(id: Long, userId: Long)
}
