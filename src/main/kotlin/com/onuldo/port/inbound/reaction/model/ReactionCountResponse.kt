package com.onuldo.port.inbound.reaction.model

import com.onuldo.domain.reaction.EmojiType

data class ReactionCountResponse(
    val recordId: Long,
    val counts: Map<EmojiType, Long>,
    val totalCount: Long
) {
    companion object {
        fun of(recordId: Long, counts: Map<EmojiType, Long>): ReactionCountResponse {
            return ReactionCountResponse(
                recordId = recordId,
                counts = counts,
                totalCount = counts.values.sum()
            )
        }
    }
}
