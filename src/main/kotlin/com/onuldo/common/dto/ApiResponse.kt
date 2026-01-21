package com.onuldo.common.dto

/**
 * 공통 API 응답 DTO
 * @param T 응답 데이터 타입
 */
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: ErrorResponse? = null
) {
    companion object {
        fun <T> success(data: T, message: String? = null): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                message = message
            )
        }

        fun <T> success(message: String): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = null,
                message = message
            )
        }

        fun <T> error(error: ErrorResponse): ApiResponse<T> {
            return ApiResponse(
                success = false,
                error = error
            )
        }

        fun <T> error(code: String, message: String): ApiResponse<T> {
            return ApiResponse(
                success = false,
                error = ErrorResponse(code, message)
            )
        }
    }
}

/**
 * 에러 응답 DTO
 */
data class ErrorResponse(
    val code: String,
    val message: String,
    val details: Map<String, Any>? = null
)

