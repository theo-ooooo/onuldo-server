package com.onuldo.application.notification

import com.onuldo.port.inbound.notification.usecase.MarkAllNotificationsAsReadUseCase
import com.onuldo.port.outbound.notification.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MarkAllNotificationsAsReadService(
    private val notificationRepository: NotificationRepository
) : MarkAllNotificationsAsReadUseCase {

    @Transactional
    override fun execute(userId: Long) {
        val unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId)
        unreadNotifications.forEach { notification ->
            notification.markAsRead()
        }
    }
}

