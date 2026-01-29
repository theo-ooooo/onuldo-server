package com.onuldo.port.outbound.reaction

import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.reaction.Reaction

interface ReactionRepository {
    fun save(reaction: Reaction): Reaction
    fun delete(reaction: Reaction)
    fun findByUserIdAndRecordIdAndEmojiType(userId: Long, recordId: Long, emojiType: EmojiType): Reaction?
    fun existsByUserIdAndRecordIdAndEmojiType(userId: Long, recordId: Long, emojiType: EmojiType): Boolean
    fun findAllByRecordId(recordId: Long): List<Reaction>
    fun countByRecordIdGroupByEmojiType(recordId: Long): Map<EmojiType, Long>
}
