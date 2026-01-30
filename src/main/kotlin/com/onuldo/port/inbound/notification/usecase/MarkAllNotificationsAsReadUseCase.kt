package com.onuldo.port.inbound.notification.usecase

interface MarkAllNotificationsAsReadUseCase {
    fun execute(userId: Long)
}

