package com.onuldo.adapter.outbound.persistence.comment

import com.onuldo.domain.comment.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentJpaRepository : JpaRepository<Comment, Long> {
    fun findByRecordId(recordId: Long): List<Comment>
    fun findByRecordIdAndIsDeletedFalse(recordId: Long): List<Comment>
    fun findByParentCommentId(parentCommentId: Long): List<Comment>
    fun countByRecordId(recordId: Long): Long
}

