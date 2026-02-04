package com.onuldo.port.inbound.notification.usecase

import com.onuldo.port.inbound.notification.model.NotificationResponse

interface GetNotificationsUseCase {
    fun execute(userId: Long): List<NotificationResponse>
}


