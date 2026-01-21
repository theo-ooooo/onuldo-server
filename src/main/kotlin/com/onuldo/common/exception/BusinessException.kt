package com.onuldo.common.exception

import org.springframework.http.HttpStatus

/**
 * 커스텀 예외
 * 
 * ErrorCode를 사용하여 에러를 정의합니다.
 * 
 * 사용 예시:
 * ```
 * throw CustomException(ErrorCode.USER_NOT_FOUND)
 * throw CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다.")
 * throw CustomException(ErrorCode.USER_NOT_FOUND, details = mapOf("userId" to userId))
 * ```
 */
open class CustomException(
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
 * 
 * 사용 예시:
 * ```
 * throw ResourceNotFoundException("타이머", timerId)
 * ```
 */
class ResourceNotFoundException(
    resourceName: String,
    resourceId: Any? = null,
    errorCode: ErrorCode = ErrorCode.RESOURCE_NOT_FOUND
) : CustomException(
    errorCode = errorCode,
    message = if (resourceId != null) {
        "$resourceName 을(를) 찾을 수 없습니다. (ID: $resourceId)"
    } else {
        "$resourceName 을(를) 찾을 수 없습니다."
    },
    details = if (resourceId != null) {
        mapOf("resourceName" to resourceName, "resourceId" to resourceId.toString())
    } else {
        mapOf("resourceName" to resourceName)
    }
)

/**
 * 중복된 리소스일 때 발생하는 예외
 * 
 * 사용 예시:
 * ```
 * throw DuplicateResourceException("취미")
 * ```
 */
class DuplicateResourceException(
    resourceName: String,
    message: String? = null,
    errorCode: ErrorCode = ErrorCode.RESOURCE_DUPLICATE
) : CustomException(
    errorCode = errorCode,
    message = message ?: "$resourceName 이(가) 이미 존재합니다.",
    details = mapOf("resourceName" to resourceName)
)

/**
 * 권한이 없을 때 발생하는 예외
 * 
 * 사용 예시:
 * ```
 * throw UnauthorizedException()
 * throw UnauthorizedException("로그인이 필요합니다.")
 * ```
 */
class UnauthorizedException(
    message: String? = null,
    errorCode: ErrorCode = ErrorCode.AUTH_UNAUTHORIZED
) : CustomException(
    errorCode = errorCode,
    message = message
)

/**
 * 접근이 금지되었을 때 발생하는 예외
 * 
 * 사용 예시:
 * ```
 * throw ForbiddenException()
 * throw ForbiddenException("이 기록에 접근할 권한이 없습니다.")
 * ```
 */
class ForbiddenException(
    message: String? = null,
    errorCode: ErrorCode = ErrorCode.AUTH_FORBIDDEN
) : CustomException(
    errorCode = errorCode,
    message = message
)

