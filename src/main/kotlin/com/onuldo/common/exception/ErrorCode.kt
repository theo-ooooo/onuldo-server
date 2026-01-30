package com.onuldo.common.exception

import org.springframework.http.HttpStatus

/**
 * 에러 코드 정의
 * 
 * 형식: {도메인}_{에러타입}
 * 예: USER_NOT_FOUND, TIMER_ALREADY_STARTED
 */
enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    // 공통 에러 (COMMON_*)
    COMMON_INTERNAL_SERVER_ERROR(
        code = "COMMON_INTERNAL_SERVER_ERROR",
        message = "서버 내부 오류가 발생했습니다.",
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    ),
    COMMON_INVALID_INPUT(
        code = "COMMON_INVALID_INPUT",
        message = "입력값이 올바르지 않습니다.",
        httpStatus = HttpStatus.BAD_REQUEST
    ),
    COMMON_VALIDATION_ERROR(
        code = "COMMON_VALIDATION_ERROR",
        message = "입력값 검증에 실패했습니다.",
        httpStatus = HttpStatus.BAD_REQUEST
    ),
    COMMON_ILLEGAL_ARGUMENT(
        code = "COMMON_ILLEGAL_ARGUMENT",
        message = "잘못된 인자가 전달되었습니다.",
        httpStatus = HttpStatus.BAD_REQUEST
    ),

    // 인증/인가 에러 (AUTH_*)
    AUTH_UNAUTHORIZED(
        code = "AUTH_UNAUTHORIZED",
        message = "인증이 필요합니다.",
        httpStatus = HttpStatus.UNAUTHORIZED
    ),
    AUTH_FORBIDDEN(
        code = "AUTH_FORBIDDEN",
        message = "접근 권한이 없습니다.",
        httpStatus = HttpStatus.FORBIDDEN
    ),
    AUTH_TOKEN_EXPIRED(
        code = "AUTH_TOKEN_EXPIRED",
        message = "토큰이 만료되었습니다.",
        httpStatus = HttpStatus.UNAUTHORIZED
    ),
    AUTH_TOKEN_INVALID(
        code = "AUTH_TOKEN_INVALID",
        message = "유효하지 않은 토큰입니다.",
        httpStatus = HttpStatus.UNAUTHORIZED
    ),

    // 리소스 에러 (RESOURCE_*)
    RESOURCE_NOT_FOUND(
        code = "RESOURCE_NOT_FOUND",
        message = "요청한 리소스를 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),
    RESOURCE_DUPLICATE(
        code = "RESOURCE_DUPLICATE",
        message = "이미 존재하는 리소스입니다.",
        httpStatus = HttpStatus.CONFLICT
    ),
    RESOURCE_ALREADY_EXISTS(
        code = "RESOURCE_ALREADY_EXISTS",
        message = "이미 존재하는 리소스입니다.",
        httpStatus = HttpStatus.CONFLICT
    ),

    // 사용자 에러 (USER_*)
    USER_NOT_FOUND(
        code = "USER_NOT_FOUND",
        message = "사용자를 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),
    USER_ALREADY_EXISTS(
        code = "USER_ALREADY_EXISTS",
        message = "이미 존재하는 사용자입니다.",
        httpStatus = HttpStatus.CONFLICT
    ),
    USER_EMAIL_DUPLICATE(
        code = "USER_EMAIL_DUPLICATE",
        message = "이미 사용 중인 이메일입니다.",
        httpStatus = HttpStatus.CONFLICT
    ),
    USER_NICKNAME_DUPLICATE(
        code = "USER_NICKNAME_DUPLICATE",
        message = "이미 사용 중인 닉네임입니다.",
        httpStatus = HttpStatus.CONFLICT
    ),

    // 타이머 에러 (TIMER_*)
    TIMER_NOT_FOUND(
        code = "TIMER_NOT_FOUND",
        message = "타이머를 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),
    TIMER_ALREADY_STARTED(
        code = "TIMER_ALREADY_STARTED",
        message = "이미 시작된 타이머가 있습니다.",
        httpStatus = HttpStatus.CONFLICT
    ),
    TIMER_NOT_STARTED(
        code = "TIMER_NOT_STARTED",
        message = "시작되지 않은 타이머입니다.",
        httpStatus = HttpStatus.BAD_REQUEST
    ),

    // 취미 에러 (HOBBY_*)
    HOBBY_NOT_FOUND(
        code = "HOBBY_NOT_FOUND",
        message = "취미를 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),
    HOBBY_ALREADY_EXISTS(
        code = "HOBBY_ALREADY_EXISTS",
        message = "이미 존재하는 취미입니다.",
        httpStatus = HttpStatus.CONFLICT
    ),

    // 기록 에러 (RECORD_*)
    RECORD_NOT_FOUND(
        code = "RECORD_NOT_FOUND",
        message = "기록을 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),
    RECORD_ACCESS_DENIED(
        code = "RECORD_ACCESS_DENIED",
        message = "기록에 접근할 권한이 없습니다.",
        httpStatus = HttpStatus.FORBIDDEN
    ),

    // 피드 에러 (FEED_*)
    FEED_NOT_FOUND(
        code = "FEED_NOT_FOUND",
        message = "피드를 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),

    // 팔로우 에러 (FOLLOW_*)
    FOLLOW_SELF_NOT_ALLOWED(
        code = "FOLLOW_SELF_NOT_ALLOWED",
        message = "자기 자신을 팔로우할 수 없습니다.",
        httpStatus = HttpStatus.BAD_REQUEST
    ),
    FOLLOW_ALREADY_EXISTS(
        code = "FOLLOW_ALREADY_EXISTS",
        message = "이미 팔로우 중인 사용자입니다.",
        httpStatus = HttpStatus.CONFLICT
    ),
    FOLLOW_NOT_FOUND(
        code = "FOLLOW_NOT_FOUND",
        message = "팔로우 관계를 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),

    // 리액션 에러 (REACTION_*)
    REACTION_NOT_FOUND(
        code = "REACTION_NOT_FOUND",
        message = "리액션을 찾을 수 없습니다.",
        httpStatus = HttpStatus.NOT_FOUND
    ),
    REACTION_ALREADY_EXISTS(
        code = "REACTION_ALREADY_EXISTS",
        message = "이미 동일한 리액션이 존재합니다.",
        httpStatus = HttpStatus.CONFLICT
    );
}


