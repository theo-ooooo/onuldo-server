package com.onuldo.port.inbound.comment.model

import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val userId: Long,
    val userNickname: String,
    val userProfileImageUrl: String?,
    val recordId: Long,
    val parentCommentId: Long?,
    val content: String,
    val replyCount: Int = 0,
    val replies: List<CommentResponse> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

