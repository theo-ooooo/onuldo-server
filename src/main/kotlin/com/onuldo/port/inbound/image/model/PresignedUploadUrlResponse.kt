package com.onuldo.port.inbound.image.model

data class PresignedUploadUrlResponse(
    val uploadUrl: String,
    val imageKey: String,
    val expiresIn: Int // minutes
)


