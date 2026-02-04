package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.notification.model.NotificationResponse
import com.onuldo.port.inbound.notification.usecase.GetNotificationsUseCase
import com.onuldo.port.inbound.notification.usecase.GetUnreadNotificationCountUseCase
import com.onuldo.port.inbound.notification.usecase.GetUnreadNotificationsUseCase
import com.onuldo.port.inbound.notification.usecase.MarkAllNotificationsAsReadUseCase
import com.onuldo.port.inbound.notification.usecase.MarkNotificationAsReadUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val securityUtils: SecurityUtils,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val getUnreadNotificationsUseCase: GetUnreadNotificationsUseCase,
    private val getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val markAllNotificationsAsReadUseCase: MarkAllNotificationsAsReadUseCase
) {

    @Operation(summary = "알림 목록 조회", description = "사용자의 모든 알림 목록을 조회합니다.")
    @GetMapping
    fun getNotifications(request: HttpServletRequest): ApiResponse<List<NotificationResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val notifications = getNotificationsUseCase.execute(userId)
        return ApiResponse.success(notifications, message = "알림 목록을 조회했습니다.")
    }

    @Operation(summary = "미읽음 알림 조회", description = "사용자의 미읽음 알림 목록을 조회합니다.")
    @GetMapping("/unread")
    fun getUnreadNotifications(request: HttpServletRequest): ApiResponse<List<NotificationResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val notifications = getUnreadNotificationsUseCase.execute(userId)
        return ApiResponse.success(notifications, message = "미읽음 알림 목록을 조회했습니다.")
    }

    @Operation(summary = "미읽음 알림 개수 조회", description = "사용자의 미읽음 알림 개수를 조회합니다.")
    @GetMapping("/unread/count")
    fun getUnreadNotificationCount(request: HttpServletRequest): ApiResponse<Long> {
        val userId = securityUtils.getCurrentUserId(request)
        val count = getUnreadNotificationCountUseCase.execute(userId)
        return ApiResponse.success(count, message = "미읽음 알림 개수를 조회했습니다.")
    }

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 처리합니다.")
    @PutMapping("/{notificationId}/read")
    fun markNotificationAsRead(
        request: HttpServletRequest,
        @PathVariable notificationId: Long
    ): ApiResponse<Unit> {
        val userId = securityUtils.getCurrentUserId(request)
        markNotificationAsReadUseCase.execute(userId, notificationId)
        return ApiResponse.success(message = "알림을 읽음 처리했습니다.")
    }

    @Operation(summary = "모든 알림 읽음 처리", description = "사용자의 모든 알림을 읽음 처리합니다.")
    @PutMapping("/read-all")
    fun markAllNotificationsAsRead(request: HttpServletRequest): ApiResponse<Unit> {
        val userId = securityUtils.getCurrentUserId(request)
        markAllNotificationsAsReadUseCase.execute(userId)
        return ApiResponse.success(message = "모든 알림을 읽음 처리했습니다.")
    }
}


