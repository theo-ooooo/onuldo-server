package com.onuldo.port.inbound.reaction.model

import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.reaction.Reaction
import java.time.LocalDateTime

data class ReactionResponse(
    val id: Long,
    val userId: Long,
    val recordId: Long,
    val emojiType: EmojiType,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(reaction: Reaction): ReactionResponse {
            return ReactionResponse(
                id = reaction.id!!,
                userId = reaction.userId,
                recordId = reaction.recordId,
                emojiType = reaction.emojiType,
                createdAt = reaction.createdAt
            )
        }
    }
}
