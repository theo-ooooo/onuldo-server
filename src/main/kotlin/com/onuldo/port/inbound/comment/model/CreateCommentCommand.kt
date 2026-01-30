package com.onuldo.port.inbound.comment.model

data class CreateCommentCommand(
    val userId: Long,
    val recordId: Long,
    val parentCommentId: Long? = null,
    val content: String
)

