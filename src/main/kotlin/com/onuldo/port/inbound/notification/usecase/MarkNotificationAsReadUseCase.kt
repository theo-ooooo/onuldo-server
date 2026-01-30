package com.onuldo.port.inbound.notification.usecase

interface MarkNotificationAsReadUseCase {
    fun execute(userId: Long, notificationId: Long)
}

