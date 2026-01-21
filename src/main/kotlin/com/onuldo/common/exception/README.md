# Exception 패키지

예외 처리 관련 클래스들을 정의합니다.

## 구조

- `ErrorCode`: 에러 코드 enum 정의
- `BusinessException`: 비즈니스 예외 기본 클래스
- `ResourceNotFoundException`: 리소스를 찾을 수 없을 때
- `DuplicateResourceException`: 중복된 리소스일 때
- `UnauthorizedException`: 인증이 필요할 때
- `ForbiddenException`: 접근 권한이 없을 때
- `GlobalExceptionHandler`: 전역 예외 처리 핸들러

## ErrorCode 사용법

### 기본 사용

```kotlin
// ErrorCode enum 사용
throw BusinessException(
    errorCode = ErrorCode.USER_NOT_FOUND,
    message = "사용자를 찾을 수 없습니다."
)

// 커스텀 메시지와 상세 정보 포함
throw BusinessException(
    errorCode = ErrorCode.TIMER_ALREADY_STARTED,
    message = "이미 실행 중인 타이머가 있습니다.",
    details = mapOf("timerId" to timerId, "userId" to userId)
)
```

### 특정 예외 클래스 사용

```kotlin
// 리소스를 찾을 수 없을 때
throw ResourceNotFoundException(
    resourceName = "타이머",
    resourceId = timerId
)

// 중복된 리소스일 때
throw DuplicateResourceException(
    resourceName = "취미",
    message = "이미 등록된 취미입니다."
)

// 인증이 필요할 때
throw UnauthorizedException(
    message = "로그인이 필요합니다."
)

// 접근 권한이 없을 때
throw ForbiddenException(
    message = "이 기록에 접근할 권한이 없습니다."
)
```

## ErrorCode 카테고리

### COMMON_* (공통 에러)
- `COMMON_INTERNAL_SERVER_ERROR`: 서버 내부 오류
- `COMMON_INVALID_INPUT`: 잘못된 입력값
- `COMMON_VALIDATION_ERROR`: 입력값 검증 실패
- `COMMON_ILLEGAL_ARGUMENT`: 잘못된 인자

### AUTH_* (인증/인가 에러)
- `AUTH_UNAUTHORIZED`: 인증 필요
- `AUTH_FORBIDDEN`: 접근 권한 없음
- `AUTH_TOKEN_EXPIRED`: 토큰 만료
- `AUTH_TOKEN_INVALID`: 유효하지 않은 토큰

### RESOURCE_* (리소스 에러)
- `RESOURCE_NOT_FOUND`: 리소스를 찾을 수 없음
- `RESOURCE_DUPLICATE`: 중복된 리소스
- `RESOURCE_ALREADY_EXISTS`: 이미 존재하는 리소스

### USER_* (사용자 에러)
- `USER_NOT_FOUND`: 사용자를 찾을 수 없음
- `USER_ALREADY_EXISTS`: 이미 존재하는 사용자
- `USER_EMAIL_DUPLICATE`: 중복된 이메일

### TIMER_* (타이머 에러)
- `TIMER_NOT_FOUND`: 타이머를 찾을 수 없음
- `TIMER_ALREADY_STARTED`: 이미 시작된 타이머
- `TIMER_NOT_STARTED`: 시작되지 않은 타이머

### HOBBY_* (취미 에러)
- `HOBBY_NOT_FOUND`: 취미를 찾을 수 없음
- `HOBBY_ALREADY_EXISTS`: 이미 존재하는 취미

### RECORD_* (기록 에러)
- `RECORD_NOT_FOUND`: 기록을 찾을 수 없음
- `RECORD_ACCESS_DENIED`: 기록 접근 권한 없음

### FEED_* (피드 에러)
- `FEED_NOT_FOUND`: 피드를 찾을 수 없음

### FOLLOW_* (팔로우 에러)
- `FOLLOW_SELF_NOT_ALLOWED`: 자기 자신 팔로우 불가
- `FOLLOW_ALREADY_EXISTS`: 이미 팔로우 중
- `FOLLOW_NOT_FOUND`: 팔로우 관계를 찾을 수 없음

## 응답 형식

모든 예외는 `GlobalExceptionHandler`에 의해 다음 형식으로 변환됩니다:

```json
{
  "success": false,
  "error": {
    "code": "USER_NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다.",
    "details": {
      "resourceName": "사용자",
      "resourceId": "123"
    }
  }
}
```

## 새로운 ErrorCode 추가하기

1. `ErrorCode.kt`에 새로운 enum 값 추가
2. 필요시 새로운 예외 클래스 생성
3. `GlobalExceptionHandler`에서 처리 (필요시)

예시:
```kotlin
// ErrorCode.kt에 추가
COMMENT_NOT_FOUND(
    code = "COMMENT_NOT_FOUND",
    message = "댓글을 찾을 수 없습니다.",
    httpStatus = HttpStatus.NOT_FOUND
)
```

