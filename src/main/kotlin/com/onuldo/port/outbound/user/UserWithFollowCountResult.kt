package com.onuldo.port.outbound.user

data class UserWithFollowCountResult(
    val userId: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val bio: String?,
    val followerCount: Long,
    val followingCount: Long
)
