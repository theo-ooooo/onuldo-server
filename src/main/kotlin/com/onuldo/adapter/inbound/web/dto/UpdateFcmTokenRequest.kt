package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.Size

/**
 * FCM 토큰 업데이트 요청 DTO
 */
data class UpdateFcmTokenRequest(
    @field:Size(max = 500, message = "FCM 토큰은 500자 이하여야 합니다.")
    val fcmToken: String?
)

