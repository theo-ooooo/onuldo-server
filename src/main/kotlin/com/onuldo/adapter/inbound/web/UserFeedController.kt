package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.feed.model.FeedItemResponse
import com.onuldo.port.inbound.feed.model.FeedQuery
import com.onuldo.port.inbound.feed.model.FeedSortType
import com.onuldo.port.inbound.feed.model.FeedType
import com.onuldo.port.inbound.feed.usecase.GetFeedUseCase
import com.onuldo.port.inbound.user.usecase.GetUserUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Feed", description = "사용자 피드 API")
@RestController
@RequestMapping("/api/users/{userId}/feed")
class UserFeedController(
    private val securityUtils: SecurityUtils,
    private val getUserUseCase: GetUserUseCase,
    private val getFeedUseCase: GetFeedUseCase
) {

    @Operation(summary = "사용자 피드 조회", description = "특정 사용자의 프로필 피드(기록 목록)를 조회합니다.")
    @GetMapping
    fun getUserFeed(
        request: HttpServletRequest,
        @PathVariable userId: Long,
        @RequestParam(defaultValue = "LATEST") sortType: FeedSortType,
        @RequestParam(required = false) hobbyId: Long?,
        @RequestParam(required = false) tagName: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ApiResponse<List<FeedItemResponse>> {
        // 대상 사용자 존재 확인 (없으면 예외 발생)
        getUserUseCase.execute(userId)

        val currentUserId = securityUtils.getCurrentUserId(request)
        val query = FeedQuery(
            userId = currentUserId,
            targetUserId = userId,
            // 팔로우 여부에 따른 공개 범위(FOLLOWERS) 노출을 위해 followingIds 계산이 필요
            feedType = FeedType.FOLLOWING,
            sortType = sortType,
            hobbyId = hobbyId,
            tagName = tagName,
            page = page,
            size = size
        )
        val feed = getFeedUseCase.execute(query)
        return ApiResponse.success(feed, message = "사용자 피드를 조회했습니다.")
    }
}


