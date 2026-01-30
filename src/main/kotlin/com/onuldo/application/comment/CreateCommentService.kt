package com.onuldo.application.comment

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.comment.Comment
import com.onuldo.port.inbound.comment.model.CommentResponse
import com.onuldo.port.inbound.comment.model.CreateCommentCommand
import com.onuldo.port.inbound.comment.usecase.CreateCommentUseCase
import com.onuldo.port.outbound.comment.CommentRepository
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateCommentService(
    private val commentRepository: CommentRepository,
    private val recordRepository: RecordRepository,
    private val userRepository: UserRepository
) : CreateCommentUseCase {

    @Transactional
    override fun execute(command: CreateCommentCommand): CommentResponse {
        // Validate record exists
        recordRepository.findById(command.recordId)
            ?: throw ResourceNotFoundException("기록", command.recordId, ErrorCode.RECORD_NOT_FOUND)

        // Validate parent comment if it's a reply
        command.parentCommentId?.let { parentId ->
            val parentComment = commentRepository.findById(parentId)
                ?: throw ResourceNotFoundException("댓글", parentId)
            if (parentComment.recordId != command.recordId) {
                throw CustomException(ErrorCode.COMMON_INVALID_INPUT, "대댓글은 같은 기록의 댓글에만 작성할 수 있습니다.")
            }
        }

        val comment = Comment(
            userId = command.userId,
            recordId = command.recordId,
            parentCommentId = command.parentCommentId,
            content = command.content
        )

        val savedComment = commentRepository.save(comment)
        val commentId = savedComment.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "댓글 ID가 없습니다.")

        // Get user info
        val user = userRepository.findById(command.userId)
            ?: throw ResourceNotFoundException("사용자", command.userId, ErrorCode.USER_NOT_FOUND)

        return CommentResponse(
            id = commentId,
            userId = savedComment.userId,
            userNickname = user.nickname,
            userProfileImageUrl = user.profileImageUrl,
            recordId = savedComment.recordId,
            parentCommentId = savedComment.parentCommentId,
            content = savedComment.content,
            replyCount = 0,
            replies = emptyList(),
            createdAt = savedComment.createdAt ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "생성 시간이 없습니다."),
            updatedAt = savedComment.updatedAt ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "수정 시간이 없습니다.")
        )
    }
}

