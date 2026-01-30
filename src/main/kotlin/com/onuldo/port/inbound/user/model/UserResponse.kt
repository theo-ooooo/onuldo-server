package com.onuldo.port.inbound.user.model

data class UserResponse(
    val userId: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val bio: String?,
    val followerCount: Long,
    val followingCount: Long
)
