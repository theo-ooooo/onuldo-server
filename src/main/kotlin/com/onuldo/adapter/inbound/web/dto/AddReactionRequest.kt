package com.onuldo.adapter.inbound.web.dto

import com.onuldo.domain.reaction.EmojiType
import jakarta.validation.constraints.NotNull

data class AddReactionRequest(
    @field:NotNull(message = "이모지 타입은 필수입니다.")
    val emojiType: EmojiType
)
