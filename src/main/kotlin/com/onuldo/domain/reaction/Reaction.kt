package com.onuldo.domain.reaction

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 리액션 엔티티
 */
@Entity
@Table(
    name = "reactions",
    indexes = [
        jakarta.persistence.Index(name = "idx_reaction_user", columnList = "user_id"),
        jakarta.persistence.Index(name = "idx_reaction_record", columnList = "record_id"),
        jakarta.persistence.Index(
            name = "uk_reaction_user_record",
            columnList = "user_id,record_id,emoji_type",
            unique = true
        )
    ]
)
class Reaction(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "record_id", nullable = false)
    val recordId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji_type", nullable = false, length = 20)
    val emojiType: EmojiType
) : BaseEntity()

