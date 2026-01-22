package com.onuldo.common.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.dto.ErrorResponse
import com.onuldo.common.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

/**
 * 인가 실패 시 처리하는 Handler
 * 인증은 되었지만 권한이 없을 때 호출됩니다.
 */
@Component
class JwtAccessDeniedHandler : AccessDeniedHandler {

    private val objectMapper = jacksonObjectMapper()

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpServletResponse.SC_FORBIDDEN

        val errorResponse = ErrorResponse(
            code = ErrorCode.AUTH_FORBIDDEN.code,
            message = "접근 권한이 없습니다."
        )

        val apiResponse = ApiResponse.error<Nothing>(errorResponse)
        objectMapper.writeValue(response.outputStream, apiResponse)
    }
}

