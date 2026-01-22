package com.onuldo.domain.comment

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

/**
 * 댓글 엔티티
 */
@Entity
@Table(
    name = "comments",
    indexes = [
        jakarta.persistence.Index(name = "idx_comment_user", columnList = "user_id"),
        jakarta.persistence.Index(name = "idx_comment_record", columnList = "record_id"),
        jakarta.persistence.Index(name = "idx_comment_parent", columnList = "parent_comment_id")
    ]
)
class Comment(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "record_id", nullable = false)
    val recordId: Long,

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    val parentComment: Comment? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
) : BaseEntity() {

    fun updateContent(newContent: String) {
        if (isDeleted) {
            throw IllegalStateException("삭제된 댓글은 수정할 수 없습니다.")
        }
        this.content = newContent
    }

    fun delete() {
        this.isDeleted = true
        this.content = "삭제된 댓글입니다."
    }

    fun isReply(): Boolean = parentComment != null
}

