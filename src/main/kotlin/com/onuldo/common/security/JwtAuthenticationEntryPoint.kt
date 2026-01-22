package com.onuldo.common.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.dto.ErrorResponse
import com.onuldo.common.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * 인증 실패 시 처리하는 EntryPoint
 * JWT 토큰이 없거나 유효하지 않을 때 호출됩니다.
 */
@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val objectMapper = jacksonObjectMapper()

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val errorResponse = ErrorResponse(
            code = ErrorCode.AUTH_UNAUTHORIZED.code,
            message = "인증이 필요합니다. 유효한 JWT 토큰을 제공해주세요."
        )

        val apiResponse = ApiResponse.error<Nothing>(errorResponse)
        objectMapper.writeValue(response.outputStream, apiResponse)
    }
}

