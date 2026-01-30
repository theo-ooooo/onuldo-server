package com.onuldo.application.comment

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ForbiddenException
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.comment.usecase.DeleteCommentUseCase
import com.onuldo.port.outbound.comment.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteCommentService(
    private val commentRepository: CommentRepository
) : DeleteCommentUseCase {

    @Transactional
    override fun execute(userId: Long, commentId: Long) {
        val comment = commentRepository.findById(commentId)
            ?: throw ResourceNotFoundException("댓글", commentId)

        if (comment.userId != userId) {
            throw ForbiddenException("댓글을 삭제할 권한이 없습니다.")
        }

        if (comment.isDeleted) {
            throw CustomException(ErrorCode.COMMON_INVALID_INPUT, "이미 삭제된 댓글입니다.")
        }

        comment.delete()
        commentRepository.save(comment)
    }
}

