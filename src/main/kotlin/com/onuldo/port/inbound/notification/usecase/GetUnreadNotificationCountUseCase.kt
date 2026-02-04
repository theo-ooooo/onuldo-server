package com.onuldo.port.inbound.notification.usecase

interface GetUnreadNotificationCountUseCase {
    fun execute(userId: Long): Long
}


