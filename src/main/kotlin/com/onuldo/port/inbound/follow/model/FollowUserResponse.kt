package com.onuldo.port.inbound.follow.model

data class FollowUserResponse(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val isFollowing: Boolean
)

data class FollowCountResponse(
    val followingCount: Long,
    val followerCount: Long
)
