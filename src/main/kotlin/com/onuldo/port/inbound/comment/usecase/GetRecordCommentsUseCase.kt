package com.onuldo.port.inbound.comment.usecase

import com.onuldo.port.inbound.comment.model.CommentResponse

interface GetRecordCommentsUseCase {
    fun execute(recordId: Long): List<CommentResponse>
}

