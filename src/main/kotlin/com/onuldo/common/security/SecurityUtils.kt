package com.onuldo.common.security

import com.onuldo.common.exception.UnauthorizedException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class SecurityUtils(
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun getCurrentUserId(request: HttpServletRequest): Long {
        val token = resolveToken(request)
            ?: throw UnauthorizedException("인증 토큰이 필요합니다.")

        if (!jwtTokenProvider.validateToken(token) || !jwtTokenProvider.isAccessToken(token)) {
            throw UnauthorizedException("유효하지 않은 토큰입니다.")
        }

        return jwtTokenProvider.getUserId(token)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearer = request.getHeader("Authorization") ?: return null
        return if (bearer.startsWith("Bearer ", ignoreCase = true)) {
            bearer.substring(7)
        } else {
            null
        }
    }
}
