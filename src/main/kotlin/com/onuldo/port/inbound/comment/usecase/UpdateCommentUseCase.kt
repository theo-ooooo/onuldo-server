package com.onuldo.port.inbound.comment.usecase

import com.onuldo.port.inbound.comment.model.CommentResponse

interface UpdateCommentUseCase {
    fun execute(userId: Long, commentId: Long, content: String): CommentResponse
}

