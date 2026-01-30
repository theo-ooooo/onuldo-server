package com.onuldo.adapter.outbound.persistence.user.dto

data class UserWithFollowCountDto(
    val userId: Long,
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val bio: String?,
    val followerCount: Long,
    val followingCount: Long
)
