package com.onuldo.domain.comment

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 댓글 엔티티
 */
@Entity
@Table(
    name = "comments",
    indexes = [
        Index(name = "idx_comment_user", columnList = "user_id"),
        Index(name = "idx_comment_record", columnList = "record_id"),
        Index(name = "idx_comment_parent", columnList = "parent_comment_id")
    ]
)
class Comment(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "record_id", nullable = false)
    val recordId: Long,

    @Column(name = "parent_comment_id")
    val parentCommentId: Long? = null,

    @Column(nullable = false, length = 1000)
    var content: String,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
) : BaseEntity() {

    fun updateContent(newContent: String) {
        this.content = newContent
    }

    fun delete() {
        this.isDeleted = true
    }

    fun isReply(): Boolean = parentCommentId != null
}

