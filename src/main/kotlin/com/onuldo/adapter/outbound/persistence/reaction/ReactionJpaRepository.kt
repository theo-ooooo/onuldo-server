package com.onuldo.adapter.outbound.persistence.reaction

import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.reaction.Reaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReactionJpaRepository : JpaRepository<Reaction, Long> {
    fun findByUserIdAndRecordIdAndEmojiType(userId: Long, recordId: Long, emojiType: EmojiType): Reaction?
    fun existsByUserIdAndRecordIdAndEmojiType(userId: Long, recordId: Long, emojiType: EmojiType): Boolean
    fun findAllByRecordId(recordId: Long): List<Reaction>

    @Query("SELECT r.emojiType, COUNT(r) FROM Reaction r WHERE r.recordId = :recordId GROUP BY r.emojiType")
    fun countByRecordIdGroupByEmojiType(recordId: Long): List<Array<Any>>
}
