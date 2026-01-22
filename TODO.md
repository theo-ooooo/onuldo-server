# Onuldo 개발 TODO 리스트

## ✅ 완료된 작업

### 인증/인가
- [x] 사용자 회원가입/로그인
- [x] JWT 토큰 발급 및 검증
- [x] 리프레시 토큰 Redis 관리
- [x] Spring Security JWT 설정

### 인프라
- [x] Spring Boot 4.0.1 설정
- [x] H2 Database 설정
- [x] Redis 설정
- [x] QueryDSL 설정
- [x] Kotlin Coroutines 설정
- [x] Swagger/OpenAPI 설정

### 도메인 모델
- [x] User (사용자)
- [x] Hobby (취미)
- [x] Timer (타이머)
- [x] Record (기록)
- [x] RecordImage (기록 이미지)
- [x] Tag (태그)
- [x] RecordTag (기록-태그 연결)
- [x] Follow (팔로우)
- [x] Reaction (리액션)
- [x] Comment (댓글)
- [x] Notification (알림)
- [x] UserHobby (사용자-취미 통계)

### 기본 기능
- [x] 취미 목록 조회 API

---

## 🔨 구현 필요 작업

### 1. 타이머 기능 (Timer)

#### 도메인/포트
- [ ] TimerRepository 인터페이스 (port/outbound/timer)
- [ ] TimerRepository 구현 (adapter/outbound/persistence/timer)

#### UseCase
- [ ] StartTimerUseCase - 타이머 시작
- [ ] PauseTimerUseCase - 타이머 일시정지
- [ ] ResumeTimerUseCase - 타이머 재개
- [ ] StopTimerUseCase - 타이머 종료
- [ ] GetCurrentTimerUseCase - 현재 실행 중인 타이머 조회

#### 컨트롤러
- [ ] TimerController - 타이머 API 엔드포인트

#### DTO
- [ ] StartTimerRequest/Response
- [ ] TimerStatusResponse
- [ ] TimerDetailResponse

---

### 2. 기록 기능 (Record)

#### 도메인/포트
- [ ] RecordRepository 인터페이스 (port/outbound/record)
- [ ] RecordRepository 구현 (adapter/outbound/persistence/record)
- [ ] RecordImageRepository 인터페이스
- [ ] RecordImageRepository 구현

#### UseCase
- [ ] CreateRecordUseCase - 기록 생성 (타이머 종료 시 자동 또는 수동)
- [ ] GetRecordUseCase - 기록 상세 조회
- [ ] GetMyRecordsUseCase - 내 기록 목록 조회
- [ ] UpdateRecordUseCase - 기록 수정 (메모, 공개 범위)
- [ ] DeleteRecordUseCase - 기록 삭제
- [ ] UploadRecordImagesUseCase - 기록 이미지 업로드

#### 컨트롤러
- [ ] RecordController - 기록 API 엔드포인트

#### DTO
- [ ] CreateRecordRequest/Response
- [ ] UpdateRecordRequest
- [ ] RecordDetailResponse
- [ ] RecordListResponse
- [ ] UploadImageRequest/Response

---

### 3. 태그 기능 (Tag)

#### 도메인/포트
- [ ] TagRepository 인터페이스 (port/outbound/tag)
- [ ] TagRepository 구현 (adapter/outbound/persistence/tag)
- [ ] RecordTagRepository 인터페이스
- [ ] RecordTagRepository 구현

#### UseCase
- [ ] CreateTagUseCase - 태그 생성 (자동 또는 수동)
- [ ] GetPopularTagsUseCase - 인기 태그 조회
- [ ] SearchTagsUseCase - 태그 검색
- [ ] AddTagsToRecordUseCase - 기록에 태그 추가
- [ ] RemoveTagFromRecordUseCase - 기록에서 태그 제거

#### 컨트롤러
- [ ] TagController - 태그 API 엔드포인트

#### DTO
- [ ] TagResponse
- [ ] PopularTagResponse
- [ ] AddTagsRequest

---

### 4. 팔로우 기능 (Follow)

#### 도메인/포트
- [ ] FollowRepository 인터페이스 (port/outbound/follow)
- [ ] FollowRepository 구현 (adapter/outbound/persistence/follow)

#### UseCase
- [ ] FollowUserUseCase - 사용자 팔로우
- [ ] UnfollowUserUseCase - 사용자 언팔로우
- [ ] GetFollowersUseCase - 팔로워 목록 조회
- [ ] GetFollowingUseCase - 팔로잉 목록 조회
- [ ] CheckFollowStatusUseCase - 팔로우 상태 확인

#### 컨트롤러
- [ ] FollowController - 팔로우 API 엔드포인트

#### DTO
- [ ] FollowUserRequest
- [ ] UserListResponse
- [ ] FollowStatusResponse

---

### 5. 리액션 기능 (Reaction)

#### 도메인/포트
- [ ] ReactionRepository 인터페이스 (port/outbound/reaction)
- [ ] ReactionRepository 구현 (adapter/outbound/persistence/reaction)

#### UseCase
- [ ] AddReactionUseCase - 리액션 추가
- [ ] RemoveReactionUseCase - 리액션 제거
- [ ] GetRecordReactionsUseCase - 기록의 리액션 목록 조회
- [ ] ToggleReactionUseCase - 리액션 토글 (있으면 제거, 없으면 추가)

#### 컨트롤러
- [ ] ReactionController - 리액션 API 엔드포인트

#### DTO
- [ ] AddReactionRequest
- [ ] ReactionResponse
- [ ] ReactionListResponse

---

### 6. 댓글 기능 (Comment)

#### 도메인/포트
- [ ] CommentRepository 인터페이스 (port/outbound/comment)
- [ ] CommentRepository 구현 (adapter/outbound/persistence/comment)

#### UseCase
- [ ] CreateCommentUseCase - 댓글 작성
- [ ] CreateReplyUseCase - 대댓글 작성
- [ ] GetRecordCommentsUseCase - 기록의 댓글 목록 조회
- [ ] UpdateCommentUseCase - 댓글 수정
- [ ] DeleteCommentUseCase - 댓글 삭제 (소프트 삭제)

#### 컨트롤러
- [ ] CommentController - 댓글 API 엔드포인트

#### DTO
- [ ] CreateCommentRequest/Response
- [ ] CommentResponse
- [ ] CommentListResponse (대댓글 포함)

---

### 7. 피드 기능 (Feed)

#### UseCase
- [ ] GetFeedUseCase - 피드 조회 (팔로잉/전체)
- [ ] GetFeedByHobbyUseCase - 취미별 피드 조회
- [ ] GetFeedByTagUseCase - 태그별 피드 조회
- [ ] GetPopularFeedUseCase - 인기 피드 조회

#### 컨트롤러
- [ ] FeedController - 피드 API 엔드포인트

#### DTO
- [ ] FeedRequest (필터, 정렬 옵션)
- [ ] FeedResponse
- [ ] FeedItemResponse

#### QueryDSL 활용
- [ ] 복잡한 필터링 쿼리 (취미, 태그, 날짜 범위)
- [ ] 정렬 (최신순, 인기순)
- [ ] 페이징 처리

---

### 8. 알림 기능 (Notification)

#### 도메인/포트
- [ ] NotificationRepository 구현 (adapter/outbound/persistence/notification)

#### UseCase
- [ ] GetNotificationsUseCase - 알림 목록 조회
- [ ] GetUnreadNotificationsUseCase - 미읽음 알림 조회
- [ ] MarkNotificationAsReadUseCase - 알림 읽음 처리
- [ ] MarkAllNotificationsAsReadUseCase - 모든 알림 읽음 처리
- [ ] GetUnreadNotificationCountUseCase - 미읽음 알림 개수 조회

#### 컨트롤러
- [ ] NotificationController - 알림 API 엔드포인트

#### DTO
- [ ] NotificationResponse
- [ ] NotificationListResponse

#### 코루틴 활용
- [ ] 비동기 알림 전송 (리액션, 댓글, 팔로우 시)

---

### 9. 통계/리포트 기능 (Statistics)

#### 도메인/포트
- [ ] UserHobbyRepository 구현 (adapter/outbound/persistence/userhobby)

#### UseCase
- [ ] GetDailyStatisticsUseCase - 일일 통계
- [ ] GetWeeklyStatisticsUseCase - 주간 통계
- [ ] GetMonthlyStatisticsUseCase - 월간 통계
- [ ] GetHobbyStatisticsUseCase - 취미별 통계
- [ ] GetStreakUseCase - 연속 기록 스트릭
- [ ] GetCalendarRecordsUseCase - 캘린더 기반 기록 조회

#### 컨트롤러
- [ ] StatisticsController 완성 (현재 기본 구조만 있음)

#### DTO
- [ ] DailyStatisticsResponse
- [ ] WeeklyStatisticsResponse
- [ ] MonthlyStatisticsResponse
- [ ] HobbyStatisticsResponse
- [ ] StreakResponse
- [ ] CalendarResponse

#### 코루틴 활용
- [ ] 여러 통계를 병렬로 계산 (이미 StatisticsService에 구현됨)

---

### 10. 사용자 프로필 기능

#### UseCase
- [ ] GetUserProfileUseCase - 사용자 프로필 조회
- [ ] UpdateUserProfileUseCase - 프로필 수정
- [ ] GetUserRecordsUseCase - 특정 사용자의 기록 조회
- [ ] GetUserStatisticsUseCase - 특정 사용자의 통계 조회

#### 컨트롤러
- [ ] UserController - 사용자 프로필 API 엔드포인트

#### DTO
- [ ] UserProfileResponse
- [ ] UpdateProfileRequest

---

## 🔧 개선 필요 사항

### 보안
- [ ] JWT에서 userId 추출 유틸리티 구현
- [ ] 현재 StatisticsController의 임시 userId 처리 수정
- [ ] 인증된 사용자 정보를 컨트롤러에서 쉽게 가져오는 방법 구현

### 예외 처리
- [ ] 도메인별 커스텀 예외 추가
- [ ] 예외 메시지 국제화 (i18n)

### 검증
- [ ] DTO 검증 어노테이션 추가
- [ ] 비즈니스 로직 검증 강화

### 테스트
- [ ] 단위 테스트 작성
- [ ] 통합 테스트 작성
- [ ] API 테스트 작성

### 성능
- [ ] 캐싱 전략 수립 (Redis 활용)
- [ ] 페이징 최적화
- [ ] N+1 문제 해결

### 문서화
- [ ] API 문서 완성 (Swagger)
- [ ] 아키텍처 문서 업데이트

---

## 📝 참고사항

### 코루틴 사용 가이드
- 비동기 작업: `coroutineScope.launch { }`
- 병렬 처리: `async { }` + `awaitAll()`
- 컨트롤러: `runBlocking { }` 또는 `suspend` 함수 사용

### QueryDSL 사용 가이드
- 복잡한 쿼리는 `UserQueryRepository` 패턴 참고
- 동적 쿼리는 `BooleanExpression` 활용

### Redis 활용
- 리프레시 토큰 관리 (완료)
- 캐싱 전략 (추가 필요)
- 세션 관리 (필요시)

---

## 우선순위

1. **높음**: Timer, Record (핵심 기능)
2. **중간**: Tag, Follow, Reaction, Comment (소통 기능)
3. **낮음**: Feed, Notification, Statistics (부가 기능)

