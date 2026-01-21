package com.onuldo.common.exception

import org.springframework.http.HttpStatus

/**
 * 비즈니스 로직 예외
 * 
 * ErrorCode를 사용하여 에러를 정의합니다.
 */
open class BusinessException(
    val errorCode: ErrorCode,
    message: String? = null,
    val details: Map<String, Any>? = null,
    cause: Throwable? = null
) : RuntimeException(message ?: errorCode.message, cause) {
    
    /**
     * HTTP 상태 코드
     */
    val httpStatus: HttpStatus
        get() = errorCode.httpStatus
    
    /**
     * 에러 코드 문자열
     */
    val errorCodeString: String
        get() = errorCode.code
}

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 */
class ResourceNotFoundException(
    resourceName: String,
    resourceId: Any? = null,
    errorCode: ErrorCode = ErrorCode.RESOURCE_NOT_FOUND
) : BusinessException(
    errorCode = errorCode,
    message = if (resourceId != null) {
        "$resourceName을(를) 찾을 수 없습니다. (ID: $resourceId)"
    } else {
        "$resourceName을(를) 찾을 수 없습니다."
    },
    details = if (resourceId != null) {
        mapOf("resourceName" to resourceName, "resourceId" to resourceId.toString())
    } else {
        mapOf("resourceName" to resourceName)
    }
)

/**
 * 중복된 리소스일 때 발생하는 예외
 */
class DuplicateResourceException(
    resourceName: String,
    message: String? = null,
    errorCode: ErrorCode = ErrorCode.RESOURCE_DUPLICATE
) : BusinessException(
    errorCode = errorCode,
    message = message ?: "$resourceName이(가) 이미 존재합니다.",
    details = mapOf("resourceName" to resourceName)
)

/**
 * 권한이 없을 때 발생하는 예외
 */
class UnauthorizedException(
    message: String? = null,
    errorCode: ErrorCode = ErrorCode.AUTH_UNAUTHORIZED
) : BusinessException(
    errorCode = errorCode,
    message = message
)

/**
 * 접근이 금지되었을 때 발생하는 예외
 */
class ForbiddenException(
    message: String? = null,
    errorCode: ErrorCode = ErrorCode.AUTH_FORBIDDEN
) : BusinessException(
    errorCode = errorCode,
    message = message
)

