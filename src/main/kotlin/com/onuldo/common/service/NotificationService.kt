package com.onuldo.common.service

import com.onuldo.domain.notification.Notification
import com.onuldo.domain.notification.NotificationType
import com.onuldo.port.outbound.notification.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

/**
 * 알림 서비스
 * 코루틴을 사용하여 비동기로 알림을 전송합니다.
 */
@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val coroutineScope: CoroutineScope
) {

    /**
     * 비동기로 알림을 생성합니다.
     * 메인 로직을 블로킹하지 않고 백그라운드에서 처리됩니다.
     */
    fun sendNotificationAsync(
        userId: Long,
        notificationType: NotificationType,
        relatedUserId: Long? = null,
        relatedRecordId: Long? = null,
        relatedCommentId: Long? = null
    ) {
        coroutineScope.launch {
            val notification = Notification(
                userId = userId,
                notificationType = notificationType,
                relatedUserId = relatedUserId,
                relatedRecordId = relatedRecordId,
                relatedCommentId = relatedCommentId
            )
            notificationRepository.save(notification)
        }
    }

    /**
     * 여러 사용자에게 동시에 알림을 전송합니다.
     */
    fun sendNotificationToMultipleUsersAsync(
        userIds: List<Long>,
        notificationType: NotificationType,
        relatedUserId: Long? = null,
        relatedRecordId: Long? = null,
        relatedCommentId: Long? = null
    ) {
        coroutineScope.launch {
            userIds.forEach { userId ->
                val notification = Notification(
                    userId = userId,
                    notificationType = notificationType,
                    relatedUserId = relatedUserId,
                    relatedRecordId = relatedRecordId,
                    relatedCommentId = relatedCommentId
                )
                notificationRepository.save(notification)
            }
        }
    }
}

