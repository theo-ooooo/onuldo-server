package com.onuldo.port.inbound.comment.usecase

import com.onuldo.port.inbound.comment.model.CommentResponse
import com.onuldo.port.inbound.comment.model.CreateCommentCommand

interface CreateCommentUseCase {
    fun execute(command: CreateCommentCommand): CommentResponse
}

