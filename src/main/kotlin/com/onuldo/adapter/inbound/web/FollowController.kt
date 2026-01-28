package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.follow.model.FollowCountResponse
import com.onuldo.port.inbound.follow.model.FollowUserResponse
import com.onuldo.port.inbound.follow.usecase.FollowUserUseCase
import com.onuldo.port.inbound.follow.usecase.GetFollowCountUseCase
import com.onuldo.port.inbound.follow.usecase.GetFollowersUseCase
import com.onuldo.port.inbound.follow.usecase.GetFollowingUseCase
import com.onuldo.port.inbound.follow.usecase.UnfollowUserUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Follow", description = "팔로우 API")
@RestController
@RequestMapping("/api/users")
class FollowController(
    private val securityUtils: SecurityUtils,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingUseCase: GetFollowingUseCase,
    private val getFollowCountUseCase: GetFollowCountUseCase
) {

    @Operation(summary = "사용자 팔로우", description = "특정 사용자를 팔로우합니다.")
    @PostMapping("/{userId}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    fun followUser(
        request: HttpServletRequest,
        @PathVariable userId: Long
    ): ApiResponse<Unit> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        followUserUseCase.execute(currentUserId, userId)
        return ApiResponse.success(message = "팔로우했습니다.")
    }

    @Operation(summary = "사용자 언팔로우", description = "특정 사용자를 언팔로우합니다.")
    @DeleteMapping("/{userId}/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unfollowUser(
        request: HttpServletRequest,
        @PathVariable userId: Long
    ): ApiResponse<Unit> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        unfollowUserUseCase.execute(currentUserId, userId)
        return ApiResponse.success(message = "언팔로우했습니다.")
    }

    @Operation(summary = "팔로워 목록 조회", description = "특정 사용자의 팔로워 목록을 조회합니다.")
    @GetMapping("/{userId}/followers")
    fun getFollowers(
        request: HttpServletRequest,
        @PathVariable userId: Long
    ): ApiResponse<List<FollowUserResponse>> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        val followers = getFollowersUseCase.execute(userId, currentUserId)
        return ApiResponse.success(followers, message = "팔로워 목록을 조회했습니다.")
    }

    @Operation(summary = "팔로잉 목록 조회", description = "특정 사용자의 팔로잉 목록을 조회합니다.")
    @GetMapping("/{userId}/following")
    fun getFollowing(
        request: HttpServletRequest,
        @PathVariable userId: Long
    ): ApiResponse<List<FollowUserResponse>> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        val following = getFollowingUseCase.execute(userId, currentUserId)
        return ApiResponse.success(following, message = "팔로잉 목록을 조회했습니다.")
    }

    @Operation(summary = "팔로우 수 조회", description = "특정 사용자의 팔로워/팔로잉 수를 조회합니다.")
    @GetMapping("/{userId}/follow-count")
    fun getFollowCount(@PathVariable userId: Long): ApiResponse<FollowCountResponse> {
        val count = getFollowCountUseCase.execute(userId)
        return ApiResponse.success(count, message = "팔로우 수를 조회했습니다.")
    }
}
