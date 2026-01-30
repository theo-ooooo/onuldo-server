package com.onuldo.port.inbound.hobby.model

data class HobbyResponse(
    val id: Long,
    val userId: Long,
    val name: String,
    val description: String?,
    val iconUrl: String?,
    val colorCode: String?
)


