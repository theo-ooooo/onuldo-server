package com.onuldo.port.inbound.user.usecase

/**
 * FCM 토큰 업데이트 UseCase
 */
interface UpdateFcmTokenUseCase {
    fun execute(userId: Long, fcmToken: String?)
}


