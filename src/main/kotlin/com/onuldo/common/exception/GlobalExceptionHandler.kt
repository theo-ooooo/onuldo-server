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
            code = ErrorCode.COMMON_VALIDATION_ERROR.code,
            message = ErrorCode.COMMON_VALIDATION_ERROR.message,
            details = errors
        )

        logger.warn("Validation error: {}", errors)

        return ResponseEntity
            .status(ErrorCode.COMMON_VALIDATION_ERROR.httpStatus)
            .body(ApiResponse.error(errorResponse))
    }

    /**
     * 커스텀 예외 처리
     */
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<ApiResponse<Nothing>> {
        val errorResponse = ErrorResponse(
            code = ex.errorCodeString,
            message = ex.message ?: ex.errorCode.message,
            details = ex.details
        )

        logger.warn("Custom exception: {} - {}", ex.errorCodeString, ex.message, ex)

        return ResponseEntity
            .status(ex.httpStatus)
            .body(ApiResponse.error(errorResponse))
    }

    /**
     * 비즈니스 예외 처리 (기존 호환성 유지)
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ApiResponse<Nothing>> {
        return handleCustomException(ex)
    }

    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        val errorResponse = ErrorResponse(
            code = ErrorCode.COMMON_ILLEGAL_ARGUMENT.code,
            message = ex.message ?: ErrorCode.COMMON_ILLEGAL_ARGUMENT.message
        )

        logger.warn("Illegal argument: {}", ex.message, ex)

        return ResponseEntity
            .status(ErrorCode.COMMON_ILLEGAL_ARGUMENT.httpStatus)
            .body(ApiResponse.error(errorResponse))
    }

    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val errorResponse = ErrorResponse(
            code = ErrorCode.COMMON_INTERNAL_SERVER_ERROR.code,
            message = ErrorCode.COMMON_INTERNAL_SERVER_ERROR.message
        )

        logger.error("Unexpected error occurred", ex)

        return ResponseEntity
            .status(ErrorCode.COMMON_INTERNAL_SERVER_ERROR.httpStatus)
            .body(ApiResponse.error(errorResponse))
    }
}

