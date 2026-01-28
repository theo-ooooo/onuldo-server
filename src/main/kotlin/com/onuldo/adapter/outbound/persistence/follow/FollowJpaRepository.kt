package com.onuldo.adapter.outbound.persistence.follow

import com.onuldo.domain.follow.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FollowJpaRepository : JpaRepository<Follow, Long> {
    fun findByFollowerIdAndFollowingId(followerId: Long, followingId: Long): Follow?
    fun existsByFollowerIdAndFollowingId(followerId: Long, followingId: Long): Boolean
    fun countByFollowerId(followerId: Long): Long
    fun countByFollowingId(followingId: Long): Long

    @Query("SELECT f.followingId FROM Follow f WHERE f.followerId = :followerId")
    fun findFollowingIdsByFollowerId(followerId: Long): List<Long>

    @Query("SELECT f.followerId FROM Follow f WHERE f.followingId = :followingId")
    fun findFollowerIdsByFollowingId(followingId: Long): List<Long>
}
