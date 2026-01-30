package com.onuldo.application.notification

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ForbiddenException
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.notification.usecase.MarkNotificationAsReadUseCase
import com.onuldo.port.outbound.notification.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MarkNotificationAsReadService(
    private val notificationRepository: NotificationRepository
) : MarkNotificationAsReadUseCase {

    @Transactional
    override fun execute(userId: Long, notificationId: Long) {
        val notification = notificationRepository.findById(notificationId)
            ?: throw ResourceNotFoundException("알림", notificationId)

        if (notification.userId != userId) {
            throw ForbiddenException("알림을 읽음 처리할 권한이 없습니다.")
        }

        if (notification.isRead) {
            throw CustomException(ErrorCode.COMMON_INVALID_INPUT, "이미 읽은 알림입니다.")
        }

        notification.markAsRead()
    }
}

