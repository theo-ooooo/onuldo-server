package com.onuldo.application.notification

import com.onuldo.domain.notification.Notification
import com.onuldo.domain.notification.NotificationType
import com.onuldo.port.outbound.notification.NotificationRepository
import com.onuldo.port.outbound.notification.PushNotificationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateNotificationService(
    private val notificationRepository: NotificationRepository,
    private val pushNotificationService: PushNotificationService
) {

    @Transactional
    fun createReactionNotification(
        recordOwnerId: Long,
        actorUserId: Long,
        actorNickname: String,
        recordId: Long,
        emojiType: String
    ) {
        // 자기 자신에게는 알림을 보내지 않음
        if (recordOwnerId == actorUserId) return

        val notification = Notification(
            userId = recordOwnerId,
            type = NotificationType.REACTION,
            title = "새로운 리액션",
            content = "${actorNickname}님이 리액션했습니다.",
            relatedUserId = actorUserId,
            relatedRecordId = recordId
        )

        val saved = notificationRepository.save(notification)

        // 푸시 알림 전송 (비동기)
        pushNotificationService.sendPushNotification(
            userId = recordOwnerId,
            title = notification.title,
            body = notification.content,
            data = mapOf(
                "type" to "REACTION",
                "recordId" to recordId.toString(),
                "notificationId" to (saved.id?.toString() ?: "")
            )
        )
    }

    @Transactional
    fun createCommentNotification(
        recordOwnerId: Long,
        actorUserId: Long,
        actorNickname: String,
        recordId: Long,
        commentId: Long,
        isReply: Boolean = false
    ) {
        // 자기 자신에게는 알림을 보내지 않음
        if (recordOwnerId == actorUserId) return

        val notification = Notification(
            userId = recordOwnerId,
            type = if (isReply) NotificationType.REPLY else NotificationType.COMMENT,
            title = if (isReply) "새로운 대댓글" else "새로운 댓글",
            content = "${actorNickname}님이 댓글을 작성했습니다.",
            relatedUserId = actorUserId,
            relatedRecordId = recordId,
            relatedCommentId = commentId
        )

        val saved = notificationRepository.save(notification)

        // 푸시 알림 전송 (비동기)
        pushNotificationService.sendPushNotification(
            userId = recordOwnerId,
            title = notification.title,
            body = notification.content,
            data = mapOf(
                "type" to if (isReply) "REPLY" else "COMMENT",
                "recordId" to recordId.toString(),
                "commentId" to commentId.toString(),
                "notificationId" to (saved.id?.toString() ?: "")
            )
        )
    }

    @Transactional
    fun createFollowNotification(
        followingId: Long,
        followerId: Long,
        followerNickname: String
    ) {
        val notification = Notification(
            userId = followingId,
            type = NotificationType.FOLLOW,
            title = "새로운 팔로워",
            content = "${followerNickname}님이 팔로우하기 시작했습니다.",
            relatedUserId = followerId
        )

        val saved = notificationRepository.save(notification)

        // 푸시 알림 전송 (비동기)
        pushNotificationService.sendPushNotification(
            userId = followingId,
            title = notification.title,
            body = notification.content,
            data = mapOf(
                "type" to "FOLLOW",
                "followerId" to followerId.toString(),
                "notificationId" to (saved.id?.toString() ?: "")
            )
        )
    }
}

