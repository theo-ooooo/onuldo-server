package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.feed.model.FeedItemResponse
import com.onuldo.port.inbound.feed.model.FeedQuery
import com.onuldo.port.inbound.feed.model.FeedSortType
import com.onuldo.port.inbound.feed.model.FeedType
import com.onuldo.port.inbound.feed.usecase.GetFeedUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*

@Tag(name = "Feed", description = "피드 API")
@RestController
@RequestMapping("/api/feed")
class FeedController(
    private val securityUtils: SecurityUtils,
    private val getFeedUseCase: GetFeedUseCase
) {

    @Operation(summary = "전체 피드 조회", description = "전체 공개된 피드를 조회합니다.")
    @GetMapping
    fun getFeed(
        request: HttpServletRequest,
        @RequestParam(defaultValue = "ALL") feedType: FeedType,
        @RequestParam(defaultValue = "LATEST") sortType: FeedSortType,
        @RequestParam(required = false) hobbyId: Long?,
        @RequestParam(required = false) tagName: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ApiResponse<List<FeedItemResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val query = FeedQuery(
            userId = userId,
            feedType = feedType,
            sortType = sortType,
            hobbyId = hobbyId,
            tagName = tagName,
            page = page,
            size = size
        )
        val feed = getFeedUseCase.execute(query)
        return ApiResponse.success(feed, message = "피드를 조회했습니다.")
    }

    @Operation(summary = "팔로잉 피드 조회", description = "팔로우한 사용자들의 피드를 조회합니다.")
    @GetMapping("/following")
    fun getFollowingFeed(
        request: HttpServletRequest,
        @RequestParam(defaultValue = "LATEST") sortType: FeedSortType,
        @RequestParam(required = false) hobbyId: Long?,
        @RequestParam(required = false) tagName: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ApiResponse<List<FeedItemResponse>> {
        val userId = securityUtils.getCurrentUserId(request)
        val query = FeedQuery(
            userId = userId,
            feedType = FeedType.FOLLOWING,
            sortType = sortType,
            hobbyId = hobbyId,
            tagName = tagName,
            page = page,
            size = size
        )
        val feed = getFeedUseCase.execute(query)
        return ApiResponse.success(feed, message = "팔로잉 피드를 조회했습니다.")
    }
}
