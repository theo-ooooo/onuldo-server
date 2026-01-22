package com.onuldo.port.inbound.hobby.model

/**
 * 취미 응답 모델
 */
data class HobbyResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val iconUrl: String?,
    val colorCode: String?
)

