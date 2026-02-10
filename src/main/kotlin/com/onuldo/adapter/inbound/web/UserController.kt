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
import com.onuldo.adapter.inbound.web.dto.ChangePasswordRequest
import com.onuldo.adapter.inbound.web.dto.ConfirmProfileImageUploadRequest
import com.onuldo.adapter.inbound.web.dto.UpdateFcmTokenRequest
import com.onuldo.adapter.inbound.web.dto.UpdateProfileRequest
import com.onuldo.port.inbound.image.model.PresignedUploadUrlResponse
import com.onuldo.port.inbound.user.model.ChangePasswordCommand
import com.onuldo.port.inbound.user.model.ConfirmProfileImageUploadCommand
import com.onuldo.port.inbound.user.model.ProfileImageUploadResponse
import com.onuldo.port.inbound.user.model.UpdateProfileCommand
import com.onuldo.port.inbound.user.model.UserResponse
import com.onuldo.port.inbound.user.usecase.ChangePasswordUseCase
import com.onuldo.port.inbound.user.usecase.ConfirmProfileImageUploadUseCase
import com.onuldo.port.inbound.user.usecase.GenerateProfileImagePresignedUrlUseCase
import com.onuldo.port.inbound.user.usecase.GetUserUseCase
import com.onuldo.port.inbound.user.usecase.SearchUsersUseCase
import com.onuldo.port.inbound.user.usecase.UpdateFcmTokenUseCase
import com.onuldo.port.inbound.user.usecase.UpdateProfileUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/users")
class UserController(
    private val securityUtils: SecurityUtils,
    private val getUserUseCase: GetUserUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingUseCase: GetFollowingUseCase,
    private val getFollowCountUseCase: GetFollowCountUseCase,
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val generateProfileImagePresignedUrlUseCase: GenerateProfileImagePresignedUrlUseCase,
    private val confirmProfileImageUploadUseCase: ConfirmProfileImageUploadUseCase
) {

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    fun getMyInfo(request: HttpServletRequest): ApiResponse<UserResponse> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        val userResponse = getUserUseCase.execute(currentUserId)
        return ApiResponse.success(userResponse, message = "내 정보를 조회했습니다.")
    }

    @Operation(summary = "사용자 검색", description = "닉네임 또는 이메일로 사용자를 검색합니다.")
    @GetMapping
    fun searchUsers(
        request: HttpServletRequest,
        @RequestParam keyword: String
    ): ApiResponse<List<UserResponse>> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        val users = searchUsersUseCase.execute(keyword, currentUserId)
        return ApiResponse.success(users, message = "사용자 검색 결과입니다.")
    }

    @Operation(summary = "사용자 정보 조회", description = "특정 사용자의 정보를 조회합니다.")
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): ApiResponse<UserResponse> {
        val userResponse = getUserUseCase.execute(userId)
        return ApiResponse.success(userResponse, message = "사용자 정보를 조회했습니다.")
    }

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
    @ResponseStatus(HttpStatus.OK)
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

    @Operation(summary = "FCM 토큰 업데이트", description = "현재 로그인한 사용자의 FCM 토큰을 업데이트합니다.")
    @PutMapping("/me/fcm-token")
    fun updateFcmToken(
        request: HttpServletRequest,
        @Validated @RequestBody updateRequest: UpdateFcmTokenRequest
    ): ApiResponse<Unit> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        updateFcmTokenUseCase.execute(currentUserId, updateRequest.fcmToken)
        return ApiResponse.success(message = "FCM 토큰을 업데이트했습니다.")
    }

    @Operation(summary = "프로필 수정", description = "현재 로그인한 사용자의 프로필을 수정합니다. (닉네임, 소개 변경 가능)")
    @PutMapping("/me/profile")
    fun updateProfile(
        request: HttpServletRequest,
        @Validated @RequestBody updateRequest: UpdateProfileRequest
    ): ApiResponse<Unit> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        val command = UpdateProfileCommand(
            userId = currentUserId,
            nickname = updateRequest.nickname,
            bio = updateRequest.bio
        )
        updateProfileUseCase.execute(command)
        return ApiResponse.success(message = "프로필을 수정했습니다.")
    }

    @Operation(summary = "비밀번호 변경", description = "현재 로그인한 사용자의 비밀번호를 변경합니다.")
    @PutMapping("/me/password")
    fun changePassword(
        request: HttpServletRequest,
        @Validated @RequestBody changePasswordRequest: ChangePasswordRequest
    ): ApiResponse<Unit> {
        val currentUserId = securityUtils.getCurrentUserId(request)
        val command = ChangePasswordCommand(
            userId = currentUserId,
            currentPassword = changePasswordRequest.currentPassword,
            newPassword = changePasswordRequest.newPassword
        )
        changePasswordUseCase.execute(command)
        return ApiResponse.success(message = "비밀번호를 변경했습니다.")
    }

    @Operation(summary = "프로필 이미지 Presigned URL 생성", description = "S3에 직접 업로드하기 위한 Presigned URL을 생성합니다.")
    @GetMapping("/me/profile-image/presigned-url")
    fun generateProfileImagePresignedUrl(
        request: HttpServletRequest,
        @RequestParam fileName: String,
        @RequestParam contentType: String = "image/webp"
    ): ApiResponse<PresignedUploadUrlResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val response = generateProfileImagePresignedUrlUseCase.execute(userId, fileName, contentType)
        return ApiResponse.success(response, message = "Presigned URL을 생성했습니다.")
    }

    @Operation(summary = "프로필 이미지 업로드 확인", description = "S3에 업로드 완료된 프로필 이미지 정보를 저장합니다.")
    @PostMapping("/me/profile-image/confirm")
    @ResponseStatus(HttpStatus.CREATED)
    fun confirmProfileImageUpload(
        request: HttpServletRequest,
        @Valid @RequestBody body: ConfirmProfileImageUploadRequest
    ): ApiResponse<ProfileImageUploadResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = ConfirmProfileImageUploadCommand(
            userId = userId,
            imageKey = body.imageKey,
            fileName = body.fileName,
            fileSize = body.fileSize,
            width = body.width,
            height = body.height
        )
        val response = confirmProfileImageUploadUseCase.execute(command)
        return ApiResponse.success(response, message = "프로필 이미지 업로드를 확인했습니다.")
    }
}
