package com.onuldo.port.inbound.image.model

data class ConfirmImageUploadCommand(
    val userId: Long,
    val recordId: Long,
    val imageKey: String,
    val fileName: String,
    val fileSize: Long,
    val width: Int?,
    val height: Int?
)


