package com.onuldo.adapter.outbound.notification

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.onuldo.common.config.FcmProperties
import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.port.outbound.notification.PushNotificationService
import com.onuldo.port.outbound.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.FileInputStream
import javax.annotation.PostConstruct

/**
 * FCM 푸시 알림 서비스 구현
 */
@Service
class FcmPushNotificationService(
    private val fcmProperties: FcmProperties,
    private val userRepository: UserRepository
) : PushNotificationService {

    private val logger = LoggerFactory.getLogger(javaClass)
    private var isInitialized = false

    @PostConstruct
    fun initialize() {
        try {
            // FirebaseApp이 이미 초기화되어 있는지 확인
            if (FirebaseApp.getApps().isEmpty()) {
                val options = if (fcmProperties.serviceAccountKeyPath != null) {
                    // 파일 경로로 초기화
                    val serviceAccount = FileInputStream(fcmProperties.serviceAccountKeyPath)
                    FirebaseOptions.builder()
                        .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount))
                        .build()
                } else {
                    // 환경 변수 GOOGLE_APPLICATION_CREDENTIALS 사용
                    FirebaseOptions.builder()
                        .setCredentials(com.google.auth.oauth2.GoogleCredentials.getApplicationDefault())
                        .build()
                }
                FirebaseApp.initializeApp(options)
                isInitialized = true
                logger.info("Firebase Admin SDK initialized successfully")
            } else {
                isInitialized = true
                logger.info("Firebase Admin SDK already initialized")
            }
        } catch (e: Exception) {
            logger.warn("Failed to initialize Firebase Admin SDK. Push notifications will be disabled: ${e.message}")
            isInitialized = false
        }
    }

    override fun sendPushNotification(
        userId: Long,
        title: String,
        body: String,
        data: Map<String, String>?
    ) {
        // FCM이 초기화되지 않은 경우 아무 작업도 하지 않음
        if (!isInitialized || FirebaseApp.getApps().isEmpty()) {
            logger.debug("FCM is not initialized, skipping push notification")
            return
        }

        try {
            // 사용자 조회 및 FCM 토큰 확인
            val user = userRepository.findById(userId)
                ?: run {
                    logger.warn("User not found: $userId")
                    return
                }

            val fcmToken = user.fcmToken
            if (fcmToken.isNullOrBlank()) {
                logger.debug("User $userId has no FCM token, skipping push notification")
                return
            }

            // FCM 메시지 생성
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build()

            val messageBuilder = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)

            // 추가 데이터가 있으면 추가
            data?.let {
                messageBuilder.putAllData(it)
            }

            val message = messageBuilder.build()

            // FCM 메시지 전송
            val response = FirebaseMessaging.getInstance().send(message)
            logger.info("Successfully sent FCM message to user $userId: $response")

        } catch (e: com.google.firebase.messaging.FirebaseMessagingException) {
            logger.error("Failed to send FCM message to user $userId", e)
            // FCM 토큰이 유효하지 않은 경우 사용자의 FCM 토큰 제거
            if (e.messagingErrorCode == com.google.firebase.messaging.MessagingErrorCode.INVALID_ARGUMENT ||
                e.messagingErrorCode == com.google.firebase.messaging.MessagingErrorCode.UNREGISTERED) {
                try {
                    val user = userRepository.findById(userId)
                    user?.updateFcmToken(null)
                } catch (ex: Exception) {
                    logger.error("Failed to remove invalid FCM token for user $userId", ex)
                }
            }
        } catch (e: Exception) {
            logger.error("Unexpected error while sending FCM message to user $userId", e)
        }
    }
}

