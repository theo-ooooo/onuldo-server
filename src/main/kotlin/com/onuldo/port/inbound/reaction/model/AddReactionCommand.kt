package com.onuldo.port.inbound.reaction.model

import com.onuldo.domain.reaction.EmojiType

data class AddReactionCommand(
    val userId: Long,
    val recordId: Long,
    val emojiType: EmojiType
)
