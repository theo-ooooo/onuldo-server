package com.onuldo.domain.follow

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 팔로우 엔티티
 */
@Entity
@Table(
    name = "follows",
    indexes = [
        Index(name = "idx_follow_follower", columnList = "follower_id"),
        Index(name = "idx_follow_following", columnList = "following_id"),
        Index(
            name = "uk_follow_follower_following",
            columnList = "follower_id,following_id",
            unique = true
        )
    ]
)
class Follow(
    @Column(name = "follower_id", nullable = false)
    val followerId: Long,

    @Column(name = "following_id", nullable = false)
    val followingId: Long
) : BaseEntity() {
    init {
        require(followerId != followingId) { "자기 자신을 팔로우할 수 없습니다." }
    }
}

