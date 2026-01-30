package com.onuldo.port.inbound.comment.usecase

interface DeleteCommentUseCase {
    fun execute(userId: Long, commentId: Long)
}

