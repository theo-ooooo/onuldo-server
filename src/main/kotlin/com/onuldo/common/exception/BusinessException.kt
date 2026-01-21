package com.onuldo.common.exception

import org.springframework.http.HttpStatus

/**
 * 비즈니스 로직 예외
 */
open class BusinessException(
    val errorCode: String,
    message: String? = null,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 */
class ResourceNotFoundException(
    resourceName: String,
    resourceId: Any? = null
) : BusinessException(
    errorCode = "RESOURCE_NOT_FOUND",
    message = if (resourceId != null) {
        "$resourceName을(를) 찾을 수 없습니다. (ID: $resourceId)"
    } else {
        "$resourceName을(를) 찾을 수 없습니다."
    },
    httpStatus = HttpStatus.NOT_FOUND
)

/**
 * 중복된 리소스일 때 발생하는 예외
 */
class DuplicateResourceException(
    resourceName: String,
    message: String? = null
) : BusinessException(
    errorCode = "DUPLICATE_RESOURCE",
    message = message ?: "$resourceName이(가) 이미 존재합니다.",
    httpStatus = HttpStatus.CONFLICT
)

/**
 * 권한이 없을 때 발생하는 예외
 */
class UnauthorizedException(
    message: String = "인증이 필요합니다."
) : BusinessException(
    errorCode = "UNAUTHORIZED",
    message = message,
    httpStatus = HttpStatus.UNAUTHORIZED
)

/**
 * 접근이 금지되었을 때 발생하는 예외
 */
class ForbiddenException(
    message: String = "접근 권한이 없습니다."
) : BusinessException(
    errorCode = "FORBIDDEN",
    message = message,
    httpStatus = HttpStatus.FORBIDDEN
)

