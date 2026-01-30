package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.CreateCommentRequest
import com.onuldo.adapter.inbound.web.dto.UpdateCommentRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.comment.model.CommentResponse
import com.onuldo.port.inbound.comment.model.CreateCommentCommand
import com.onuldo.port.inbound.comment.usecase.CreateCommentUseCase
import com.onuldo.port.inbound.comment.usecase.DeleteCommentUseCase
import com.onuldo.port.inbound.comment.usecase.GetRecordCommentsUseCase
import com.onuldo.port.inbound.comment.usecase.UpdateCommentUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequestMapping("/api/records/{recordId}/comments")
class CommentController(
    private val securityUtils: SecurityUtils,
    private val createCommentUseCase: CreateCommentUseCase,
    private val getRecordCommentsUseCase: GetRecordCommentsUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) {

    @Operation(summary = "댓글 작성", description = "기록에 댓글을 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @Valid @RequestBody body: CreateCommentRequest
    ): ApiResponse<CommentResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = CreateCommentCommand(
            userId = userId,
            recordId = recordId,
            parentCommentId = body.parentCommentId,
            content = body.content
        )
        val comment = createCommentUseCase.execute(command)
        return ApiResponse.success(comment, message = "댓글을 작성했습니다.")
    }

    @Operation(summary = "댓글 목록 조회", description = "기록의 댓글 목록을 조회합니다.")
    @GetMapping
    fun getComments(@PathVariable recordId: Long): ApiResponse<List<CommentResponse>> {
        val comments = getRecordCommentsUseCase.execute(recordId)
        return ApiResponse.success(comments, message = "댓글 목록을 조회했습니다.")
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    fun updateComment(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody body: UpdateCommentRequest
    ): ApiResponse<CommentResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val comment = updateCommentUseCase.execute(userId, commentId, body.content)
        return ApiResponse.success(comment, message = "댓글을 수정했습니다.")
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @PathVariable commentId: Long
    ): ApiResponse<Unit> {
        val userId = securityUtils.getCurrentUserId(request)
        deleteCommentUseCase.execute(userId, commentId)
        return ApiResponse.success(message = "댓글을 삭제했습니다.")
    }
}

