package com.onuldo.adapter.outbound.persistence.notification

import com.onuldo.domain.notification.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationJpaRepository : JpaRepository<Notification, Long> {
    fun findByUserId(userId: Long): List<Notification>
    fun findByUserIdAndIsReadFalse(userId: Long): List<Notification>
    fun countByUserIdAndIsReadFalse(userId: Long): Long
}

