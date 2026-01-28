package com.onuldo.adapter.outbound.persistence.follow

import com.onuldo.domain.follow.Follow
import com.onuldo.port.outbound.follow.FollowRepository
import org.springframework.stereotype.Repository

@Repository
class FollowRepositoryImpl(
    private val followJpaRepository: FollowJpaRepository
) : FollowRepository {

    override fun save(follow: Follow): Follow {
        return followJpaRepository.save(follow)
    }

    override fun findByFollowerIdAndFollowingId(followerId: Long, followingId: Long): Follow? {
        return followJpaRepository.findByFollowerIdAndFollowingId(followerId, followingId)
    }

    override fun findFollowingIdsByFollowerId(followerId: Long): List<Long> {
        return followJpaRepository.findFollowingIdsByFollowerId(followerId)
    }

    override fun findFollowerIdsByFollowingId(followingId: Long): List<Long> {
        return followJpaRepository.findFollowerIdsByFollowingId(followingId)
    }

    override fun delete(follow: Follow) {
        followJpaRepository.delete(follow)
    }

    override fun existsByFollowerIdAndFollowingId(followerId: Long, followingId: Long): Boolean {
        return followJpaRepository.existsByFollowerIdAndFollowingId(followerId, followingId)
    }

    override fun countByFollowerId(followerId: Long): Long {
        return followJpaRepository.countByFollowerId(followerId)
    }

    override fun countByFollowingId(followingId: Long): Long {
        return followJpaRepository.countByFollowingId(followingId)
    }
}
