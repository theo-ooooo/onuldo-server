package com.onuldo.port.outbound.user

/**
 * 리프레시 토큰 저장소 인터페이스
 * Redis를 통해 리프레시 토큰을 관리합니다.
 */
interface RefreshTokenRepository {
    /**
     * 리프레시 토큰을 저장합니다.
     * @param userId 사용자 ID
     * @param refreshToken 리프레시 토큰
     * @param expirationSeconds 만료 시간 (초)
     */
    fun save(userId: Long, refreshToken: String, expirationSeconds: Long)

    /**
     * 리프레시 토큰을 조회합니다.
     * @param userId 사용자 ID
     * @return 저장된 리프레시 토큰, 없으면 null
     */
    fun findByUserId(userId: Long): String?

    /**
     * 리프레시 토큰을 삭제합니다.
     * @param userId 사용자 ID
     */
    fun deleteByUserId(userId: Long)

    /**
     * 리프레시 토큰이 존재하는지 확인합니다.
     * @param userId 사용자 ID
     * @param refreshToken 리프레시 토큰
     * @return 토큰이 존재하고 일치하면 true
     */
    fun exists(userId: Long, refreshToken: String): Boolean
}

