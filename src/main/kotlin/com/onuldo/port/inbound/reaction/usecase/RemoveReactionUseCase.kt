package com.onuldo.port.inbound.reaction.usecase

import com.onuldo.domain.reaction.EmojiType

interface RemoveReactionUseCase {
    fun execute(userId: Long, recordId: Long, emojiType: EmojiType)
}
