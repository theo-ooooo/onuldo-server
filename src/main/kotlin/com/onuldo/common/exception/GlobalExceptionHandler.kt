package com.onuldo.common.exception

import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.dto.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * 유효성 검증 실패 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errors = ex.bindingResult.fieldErrors.associate { fieldError: FieldError ->
            fieldError.field to (fieldError.defaultMessage ?: "Invalid value")
        }

        val errorResponse = ErrorResponse(
            code = "VALIDATION_ERROR",
            message = "입력값 검증에 실패했습니다.",
            details = errors
        )

        logger.warn("Validation error: {}", errors)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorResponse))
    }

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ApiResponse<Nothing>> {
        val errorResponse = ErrorResponse(
            code = ex.errorCode,
            message = ex.message ?: "비즈니스 로직 오류가 발생했습니다."
        )

        logger.warn("Business exception: {}", ex.message, ex)

        return ResponseEntity
            .status(ex.httpStatus)
            .body(ApiResponse.error(errorResponse))
    }

    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        val errorResponse = ErrorResponse(
            code = "ILLEGAL_ARGUMENT",
            message = ex.message ?: "잘못된 인자가 전달되었습니다."
        )

        logger.warn("Illegal argument: {}", ex.message, ex)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(errorResponse))
    }

    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val errorResponse = ErrorResponse(
            code = "INTERNAL_SERVER_ERROR",
            message = "서버 내부 오류가 발생했습니다."
        )

        logger.error("Unexpected error occurred", ex)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(errorResponse))
    }
}

