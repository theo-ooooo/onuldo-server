package com.onuldo.port.inbound.feed.model

import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.record.RecordVisibility
import java.time.LocalDate
import java.time.LocalDateTime

data class FeedItemResponse(
    val recordId: Long,
    val userId: Long,
    val userNickname: String,
    val userProfileImageUrl: String?,
    val hobbyId: Long,
    val hobbyName: String,
    val durationSeconds: Int,
    val memo: String?,
    val visibility: RecordVisibility,
    val activityDate: LocalDate,
    val tags: List<String>,
    val images: List<FeedImageResponse>,
    val reactionCounts: Map<EmojiType, Long>,
    val commentCount: Int,
    val createdAt: LocalDateTime
)

data class FeedImageResponse(
    val imageId: String,
    val imageUrl: String,
    val width: Int?,
    val height: Int?
)
