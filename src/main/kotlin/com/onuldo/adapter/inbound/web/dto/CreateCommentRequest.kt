package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCommentRequest(
    @field:NotBlank(message = "댓글 내용은 필수입니다.")
    @field:Size(max = 1000, message = "댓글 내용은 1000자 이하여야 합니다.")
    val content: String,

    val parentCommentId: Long? = null
)

