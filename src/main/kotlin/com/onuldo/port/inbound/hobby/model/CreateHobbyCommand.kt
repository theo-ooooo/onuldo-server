package com.onuldo.port.inbound.hobby.model

data class CreateHobbyCommand(
    val name: String,
    val description: String? = null,
    val iconUrl: String? = null,
    val colorCode: String? = null
)