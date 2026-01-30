package com.onuldo.port.outbound.comment

import com.onuldo.domain.comment.Comment

interface CommentRepository {
    fun save(comment: Comment): Comment
    fun findById(id: Long): Comment?
    fun findByRecordId(recordId: Long): List<Comment>
    fun findByRecordIdAndIsDeletedFalse(recordId: Long): List<Comment>
    fun findByParentCommentId(parentCommentId: Long): List<Comment>
    fun countByRecordId(recordId: Long): Long
    fun deleteById(id: Long)
}

