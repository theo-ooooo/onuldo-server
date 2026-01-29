package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.AddReactionRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.domain.reaction.EmojiType
import com.onuldo.port.inbound.reaction.model.AddReactionCommand
import com.onuldo.port.inbound.reaction.model.ReactionCountResponse
import com.onuldo.port.inbound.reaction.model.ReactionResponse
import com.onuldo.port.inbound.reaction.model.ReactionWithUserResponse
import com.onuldo.port.inbound.reaction.usecase.AddReactionUseCase
import com.onuldo.port.inbound.reaction.usecase.GetReactionCountUseCase
import com.onuldo.port.inbound.reaction.usecase.GetReactionsUseCase
import com.onuldo.port.inbound.reaction.usecase.RemoveReactionUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Reaction", description = "리액션 API")
@RestController
@RequestMapping("/api/records/{recordId}/reactions")
class ReactionController(
    private val securityUtils: SecurityUtils,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val getReactionsUseCase: GetReactionsUseCase,
    private val getReactionCountUseCase: GetReactionCountUseCase
) {

    @Operation(summary = "리액션 추가", description = "기록에 리액션을 추가합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addReaction(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @Valid @RequestBody body: AddReactionRequest
    ): ApiResponse<ReactionResponse> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        val command = AddReactionCommand(
            userId = currentUserId,
            recordId = recordId,
            emojiType = body.emojiType
        )
        val response = addReactionUseCase.execute(command)
        return ApiResponse.success(response, message = "리액션을 추가했습니다.")
    }

    @Operation(summary = "리액션 제거", description = "기록에서 리액션을 제거합니다.")
    @DeleteMapping("/{emojiType}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeReaction(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @PathVariable emojiType: EmojiType
    ): ApiResponse<Unit> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        removeReactionUseCase.execute(currentUserId, recordId, emojiType)
        return ApiResponse.success(message = "리액션을 제거했습니다.")
    }

    @Operation(summary = "리액션 목록 조회", description = "기록의 리액션 목록을 조회합니다.")
    @GetMapping
    fun getReactions(@PathVariable recordId: Long): ApiResponse<List<ReactionWithUserResponse>> {
        val reactions = getReactionsUseCase.execute(recordId)
        return ApiResponse.success(reactions, message = "리액션 목록을 조회했습니다.")
    }

    @Operation(summary = "리액션 통계 조회", description = "기록의 리액션 통계를 조회합니다.")
    @GetMapping("/count")
    fun getReactionCount(@PathVariable recordId: Long): ApiResponse<ReactionCountResponse> {
        val count = getReactionCountUseCase.execute(recordId)
        return ApiResponse.success(count, message = "리액션 통계를 조회했습니다.")
    }
}
