package com.onuldo.adapter.outbound.persistence.reaction

import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.reaction.Reaction
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.springframework.stereotype.Repository

@Repository
class ReactionRepositoryImpl(
    private val reactionJpaRepository: ReactionJpaRepository
) : ReactionRepository {

    override fun save(reaction: Reaction): Reaction {
        return reactionJpaRepository.save(reaction)
    }

    override fun delete(reaction: Reaction) {
        reactionJpaRepository.delete(reaction)
    }

    override fun findByUserIdAndRecordIdAndEmojiType(
        userId: Long,
        recordId: Long,
        emojiType: EmojiType
    ): Reaction? {
        return reactionJpaRepository.findByUserIdAndRecordIdAndEmojiType(userId, recordId, emojiType)
    }

    override fun existsByUserIdAndRecordIdAndEmojiType(
        userId: Long,
        recordId: Long,
        emojiType: EmojiType
    ): Boolean {
        return reactionJpaRepository.existsByUserIdAndRecordIdAndEmojiType(userId, recordId, emojiType)
    }

    override fun findAllByRecordId(recordId: Long): List<Reaction> {
        return reactionJpaRepository.findAllByRecordId(recordId)
    }

    override fun findAllByUserIdAndRecordId(userId: Long, recordId: Long): List<Reaction> {
        return reactionJpaRepository.findAllByUserIdAndRecordId(userId, recordId)
    }

    override fun countByRecordIdGroupByEmojiType(recordId: Long): Map<EmojiType, Long> {
        return reactionJpaRepository.countByRecordIdGroupByEmojiType(recordId)
            .associate { it[0] as EmojiType to it[1] as Long }
    }
}
