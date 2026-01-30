package com.onuldo.application.comment

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ForbiddenException
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.comment.model.CommentResponse
import com.onuldo.port.inbound.comment.usecase.UpdateCommentUseCase
import com.onuldo.port.outbound.comment.CommentRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateCommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) : UpdateCommentUseCase {

    @Transactional
    override fun execute(userId: Long, commentId: Long, content: String): CommentResponse {
        val comment = commentRepository.findById(commentId)
            ?: throw ResourceNotFoundException("댓글", commentId)

        if (comment.userId != userId) {
            throw ForbiddenException("댓글을 수정할 권한이 없습니다.")
        }

        if (comment.isDeleted) {
            throw CustomException(ErrorCode.COMMON_INVALID_INPUT, "삭제된 댓글은 수정할 수 없습니다.")
        }

        comment.updateContent(content)

        val user = userRepository.findById(userId)
            ?: throw ResourceNotFoundException("사용자", userId, ErrorCode.USER_NOT_FOUND)

        val replyCount = if (comment.parentCommentId == null) {
            commentRepository.findByParentCommentId(commentId).size.toInt()
        } else {
            0
        }

        return CommentResponse(
            id = comment.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "댓글 ID가 없습니다."),
            userId = comment.userId,
            userNickname = user.nickname,
            userProfileImageUrl = user.profileImageUrl,
            recordId = comment.recordId,
            parentCommentId = comment.parentCommentId,
            content = comment.content,
            replyCount = replyCount,
            replies = emptyList(),
            createdAt = comment.createdAt ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "생성 시간이 없습니다."),
            updatedAt = comment.updatedAt ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "수정 시간이 없습니다.")
        )
    }
}

