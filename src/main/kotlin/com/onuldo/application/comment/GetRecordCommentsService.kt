package com.onuldo.application.comment

import com.onuldo.port.inbound.comment.model.CommentResponse
import com.onuldo.port.inbound.comment.usecase.GetRecordCommentsUseCase
import com.onuldo.port.outbound.comment.CommentRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetRecordCommentsService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) : GetRecordCommentsUseCase {

    @Transactional(readOnly = true)
    override fun execute(recordId: Long): List<CommentResponse> {
        val comments = commentRepository.findByRecordIdAndIsDeletedFalse(recordId)
        val topLevelComments = comments.filter { it.parentCommentId == null }
        val replies = comments.filter { it.parentCommentId != null }
            .groupBy { it.parentCommentId }

        return topLevelComments.map { comment ->
            toResponse(comment, replies[comment.id] ?: emptyList())
        }
    }

    private fun toResponse(comment: com.onuldo.domain.comment.Comment, commentReplies: List<com.onuldo.domain.comment.Comment>): CommentResponse {
        val user = userRepository.findById(comment.userId)
            ?: throw com.onuldo.common.exception.ResourceNotFoundException("사용자", comment.userId)

        val replyResponses = commentReplies
            .filter { !it.isDeleted }
            .map { reply ->
                val replyUser = userRepository.findById(reply.userId)
                    ?: throw com.onuldo.common.exception.ResourceNotFoundException("사용자", reply.userId)
                CommentResponse(
                    id = reply.id ?: throw com.onuldo.common.exception.CustomException(com.onuldo.common.exception.ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "댓글 ID가 없습니다."),
                    userId = reply.userId,
                    userNickname = replyUser.nickname,
                    userProfileImageUrl = replyUser.profileImageUrl,
                    recordId = reply.recordId,
                    parentCommentId = reply.parentCommentId,
                    content = reply.content,
                    replyCount = 0,
                    replies = emptyList(),
                    createdAt = reply.createdAt ?: throw com.onuldo.common.exception.CustomException(com.onuldo.common.exception.ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "생성 시간이 없습니다."),
                    updatedAt = reply.updatedAt ?: throw com.onuldo.common.exception.CustomException(com.onuldo.common.exception.ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "수정 시간이 없습니다.")
                )
            }

        return CommentResponse(
            id = comment.id ?: throw com.onuldo.common.exception.CustomException(com.onuldo.common.exception.ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "댓글 ID가 없습니다."),
            userId = comment.userId,
            userNickname = user.nickname,
            userProfileImageUrl = user.profileImageUrl,
            recordId = comment.recordId,
            parentCommentId = comment.parentCommentId,
            content = comment.content,
            replyCount = replyResponses.size,
            replies = replyResponses,
            createdAt = comment.createdAt ?: throw com.onuldo.common.exception.CustomException(com.onuldo.common.exception.ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "생성 시간이 없습니다."),
            updatedAt = comment.updatedAt ?: throw com.onuldo.common.exception.CustomException(com.onuldo.common.exception.ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "수정 시간이 없습니다.")
        )
    }
}

