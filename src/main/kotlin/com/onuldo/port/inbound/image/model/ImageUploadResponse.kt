package com.onuldo.port.inbound.image.model

data class ImageUploadResponse(
    val imageId: Long,
    val imageUrl: String,
    val thumbnailUrl: String?,
    val fileName: String,
    val fileSize: Long,
    val width: Int?,
    val height: Int?
)

