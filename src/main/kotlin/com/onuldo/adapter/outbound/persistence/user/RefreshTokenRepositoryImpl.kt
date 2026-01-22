package com.onuldo.adapter.outbound.persistence.user

import com.onuldo.port.outbound.user.RefreshTokenRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : RefreshTokenRepository {

    companion object {
        private const val KEY_PREFIX = "refresh_token:"
    }

    override fun save(userId: Long, refreshToken: String, expirationSeconds: Long) {
        val key = getKey(userId)
        redisTemplate.opsForValue().set(key, refreshToken, expirationSeconds, TimeUnit.SECONDS)
    }

    override fun findByUserId(userId: Long): String? {
        val key = getKey(userId)
        return redisTemplate.opsForValue().get(key)
    }

    override fun deleteByUserId(userId: Long) {
        val key = getKey(userId)
        redisTemplate.delete(key)
    }

    override fun exists(userId: Long, refreshToken: String): Boolean {
        val storedToken = findByUserId(userId)
        return storedToken != null && storedToken == refreshToken
    }

    private fun getKey(userId: Long): String = "$KEY_PREFIX$userId"
}

