package com.onuldo.port.outbound.follow

import com.onuldo.domain.follow.Follow

interface FollowRepository {
    fun save(follow: Follow): Follow
    fun findByFollowerIdAndFollowingId(followerId: Long, followingId: Long): Follow?
    fun findFollowingIdsByFollowerId(followerId: Long): List<Long>
    fun findFollowerIdsByFollowingId(followingId: Long): List<Long>
    fun delete(follow: Follow)
    fun existsByFollowerIdAndFollowingId(followerId: Long, followingId: Long): Boolean
    fun countByFollowerId(followerId: Long): Long
    fun countByFollowingId(followingId: Long): Long
}
