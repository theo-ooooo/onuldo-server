package com.onuldo.adapter.outbound.persistence.comment

import com.onuldo.domain.comment.Comment
import com.onuldo.port.outbound.comment.CommentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl(
    private val commentJpaRepository: CommentJpaRepository
) : CommentRepository {

    override fun save(comment: Comment): Comment {
        return commentJpaRepository.save(comment)
    }

    override fun findById(id: Long): Comment? {
        return commentJpaRepository.findByIdOrNull(id)
    }

    override fun findByRecordId(recordId: Long): List<Comment> {
        return commentJpaRepository.findByRecordId(recordId)
    }

    override fun findByRecordIdAndIsDeletedFalse(recordId: Long): List<Comment> {
        return commentJpaRepository.findByRecordIdAndIsDeletedFalse(recordId)
    }

    override fun findByParentCommentId(parentCommentId: Long): List<Comment> {
        return commentJpaRepository.findByParentCommentId(parentCommentId)
    }

    override fun countByRecordId(recordId: Long): Long {
        return commentJpaRepository.countByRecordId(recordId)
    }

    override fun deleteById(id: Long) {
        commentJpaRepository.deleteById(id)
    }
}

