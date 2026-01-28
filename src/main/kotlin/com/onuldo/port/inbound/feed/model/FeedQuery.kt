package com.onuldo.port.inbound.feed.model

enum class FeedType {
    ALL,        // 전체 공개 피드
    FOLLOWING   // 팔로잉 피드
}

enum class FeedSortType {
    LATEST,     // 최신순
    POPULAR     // 인기순 (리액션 + 댓글 수)
}

data class FeedQuery(
    val userId: Long,
    val feedType: FeedType = FeedType.ALL,
    val sortType: FeedSortType = FeedSortType.LATEST,
    val hobbyId: Long? = null,
    val tagName: String? = null,
    val page: Int = 0,
    val size: Int = 20
)
