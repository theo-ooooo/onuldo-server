package com.onuldo.port.inbound.notification.model

import com.onuldo.domain.notification.NotificationType
import java.time.LocalDateTime

data class NotificationResponse(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val content: String,
    val relatedUserId: Long?,
    val relatedUserNickname: String?,
    val relatedRecordId: Long?,
    val relatedCommentId: Long?,
    val isRead: Boolean,
    val createdAt: LocalDateTime
)


