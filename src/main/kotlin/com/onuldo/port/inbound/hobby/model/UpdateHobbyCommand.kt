package com.onuldo.port.inbound.hobby.model

data class UpdateHobbyCommand(
    val id: Long,
    val name: String? = null,
    val description: String? = null,
    val iconUrl: String? = null,
    val colorCode: String? = null
)
