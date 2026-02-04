package com.onuldo.port.outbound.notification

/**
 * 푸시 알림 서비스 인터페이스
 * 나중에 FCM, APNS 등을 구현할 수 있도록 확장 가능한 구조
 */
interface PushNotificationService {
    /**
     * 푸시 알림 전송
     * @param userId 알림을 받을 사용자 ID
     * @param title 알림 제목
     * @param body 알림 내용
     * @param data 추가 데이터 (선택사항)
     */
    fun sendPushNotification(
        userId: Long,
        title: String,
        body: String,
        data: Map<String, String>? = null
    )
}


