package com.onuldo.adapter.outbound.notification

import com.onuldo.port.outbound.notification.PushNotificationService
import org.springframework.stereotype.Service

/**
 * 푸시 알림이 구현되지 않은 경우 사용하는 No-Op 구현
 * FCM이 활성화되지 않은 경우 사용
 */
@Service
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(
    name = ["fcm.enabled"],
    havingValue = "false",
    matchIfMissing = true
)
class NoOpPushNotificationService : PushNotificationService {
    override fun sendPushNotification(
        userId: Long,
        title: String,
        body: String,
        data: Map<String, String>?
    ) {
        // 현재는 아무 작업도 하지 않음
        // 나중에 FCM, APNS 등을 구현할 때 교체
    }
}

