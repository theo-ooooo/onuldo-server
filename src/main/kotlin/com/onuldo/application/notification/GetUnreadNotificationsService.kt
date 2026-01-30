package com.onuldo.application.notification

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.port.inbound.notification.model.NotificationResponse
import com.onuldo.port.inbound.notification.usecase.GetUnreadNotificationsUseCase
import com.onuldo.port.outbound.notification.NotificationRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUnreadNotificationsService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository
) : GetUnreadNotificationsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): List<NotificationResponse> {
        val notifications = notificationRepository.findByUserIdAndIsReadFalse(userId)
            .sortedByDescending { it.createdAt }

        return notifications.map { notification ->
            val relatedUser = notification.relatedUserId?.let {
                userRepository.findById(it)
            }

            NotificationResponse(
                id = notification.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "알림 ID가 없습니다."),
                type = notification.type,
                title = notification.title,
                content = notification.content,
                relatedUserId = notification.relatedUserId,
                relatedUserNickname = relatedUser?.nickname,
                relatedRecordId = notification.relatedRecordId,
                relatedCommentId = notification.relatedCommentId,
                isRead = notification.isRead,
                createdAt = notification.createdAt ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "생성 시간이 없습니다.")
            )
        }
    }
}

