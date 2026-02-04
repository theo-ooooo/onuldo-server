package com.onuldo.application.notification

import com.onuldo.port.inbound.notification.usecase.GetUnreadNotificationCountUseCase
import com.onuldo.port.outbound.notification.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUnreadNotificationCountService(
    private val notificationRepository: NotificationRepository
) : GetUnreadNotificationCountUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): Long {
        return notificationRepository.countByUserIdAndIsReadFalse(userId)
    }
}


