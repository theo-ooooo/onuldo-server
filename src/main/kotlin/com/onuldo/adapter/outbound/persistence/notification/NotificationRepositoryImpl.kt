package com.onuldo.adapter.outbound.persistence.notification

import com.onuldo.domain.notification.Notification
import com.onuldo.port.outbound.notification.NotificationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class NotificationRepositoryImpl(
    private val notificationJpaRepository: NotificationJpaRepository
) : NotificationRepository {

    override fun save(notification: Notification): Notification {
        return notificationJpaRepository.save(notification)
    }

    override fun findById(id: Long): Notification? {
        return notificationJpaRepository.findByIdOrNull(id)
    }

    override fun findByUserId(userId: Long): List<Notification> {
        return notificationJpaRepository.findByUserId(userId)
    }

    override fun findByUserIdAndIsReadFalse(userId: Long): List<Notification> {
        return notificationJpaRepository.findByUserIdAndIsReadFalse(userId)
    }

    override fun countByUserIdAndIsReadFalse(userId: Long): Long {
        return notificationJpaRepository.countByUserIdAndIsReadFalse(userId)
    }

    override fun deleteById(id: Long) {
        notificationJpaRepository.deleteById(id)
    }
}


