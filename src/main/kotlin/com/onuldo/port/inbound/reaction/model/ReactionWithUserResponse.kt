package com.onuldo.port.inbound.reaction.model

import com.onuldo.domain.reaction.EmojiType
import java.time.LocalDateTime

data class ReactionWithUserResponse(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val emojiType: EmojiType,
    val createdAt: LocalDateTime
)
