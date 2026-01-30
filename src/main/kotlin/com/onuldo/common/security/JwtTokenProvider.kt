package com.onuldo.common.security

import com.onuldo.common.config.JwtProperties
import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date

@Component
class JwtTokenProvider(
    jwtProperties: JwtProperties
) {

    private val accessTokenValidityInSeconds: Long =
        jwtProperties.accessTokenValidityInSeconds

    private val refreshTokenValidityInSeconds: Long =
        jwtProperties.refreshTokenValidityInSeconds

    private val key: Key = run {
        val secret = jwtProperties.secret
            ?: throw CustomException(
                ErrorCode.COMMON_INTERNAL_SERVER_ERROR,
                "JWT secret이 설정되지 않았습니다. 환경 변수 JWT_SECRET 또는 application.yml의 jwt.secret을 설정해주세요."
            )
        
        // JWT HMAC-SHA256은 최소 256비트(32바이트)가 필요합니다
        val secretBytes = secret.toByteArray(StandardCharsets.UTF_8)
        if (secretBytes.size < 32) {
            throw CustomException(
                ErrorCode.COMMON_INTERNAL_SERVER_ERROR,
                "JWT secret은 최소 32바이트(256비트) 이상이어야 합니다. 현재 길이: ${secretBytes.size}바이트"
            )
        }
        
        Keys.hmacShaKeyFor(secretBytes)
    }

    fun generateAccessToken(userId: Long, email: String): String =
        generateToken(userId, email, accessTokenValidityInSeconds, "ACCESS")

    fun generateRefreshToken(userId: Long, email: String): String =
        generateToken(userId, email, refreshTokenValidityInSeconds, "REFRESH")

    private fun generateToken(
        userId: Long,
        email: String,
        validityInSeconds: Long,
        type: String
    ): String {
        val now = Date()
        val expiry = Date(now.time + validityInSeconds * 1000)

        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .claim("type", type)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val email = claims.subject
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        val principal = User(email, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validateToken(token: String): Boolean =
        try {
            val claims = getClaims(token)
            !claims.expiration.before(Date())
        } catch (ex: Exception) {
            false
        }

    fun isAccessToken(token: String): Boolean =
        try {
            val claims = getClaims(token)
            claims["type"] == "ACCESS"
        } catch (ex: Exception) {
            false
        }

    fun getUserId(token: String): Long =
        getClaims(token)["userId"].toString().toLong()

    fun getEmail(token: String): String =
        getClaims(token).subject

    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
}


