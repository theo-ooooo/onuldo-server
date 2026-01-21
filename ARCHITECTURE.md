# Onuldo 아키텍처

Onuldo 프로젝트는 **헥사고날 아키텍처(Hexagonal Architecture)**를 기반으로 설계되었습니다.

## 아키텍처 개요

헥사고날 아키텍처는 포트와 어댑터 패턴을 사용하여 비즈니스 로직을 외부 의존성으로부터 격리합니다.

```
┌─────────────────────────────────────────┐
│         Adapter Layer (어댑터)          │
│  ┌──────────────┐    ┌──────────────┐  │
│  │ Inbound      │    │ Outbound     │  │
│  │ - Web        │    │ - Persistence│  │
│  │ - Event      │    │ - External   │  │
│  └──────────────┘    └──────────────┘  │
└─────────────────────────────────────────┘
              │              │
              ▼              ▼
┌─────────────────────────────────────────┐
│         Port Layer (포트)                │
│  ┌──────────────┐    ┌──────────────┐  │
│  │ Inbound      │    │ Outbound     │  │
│  │ Ports        │    │ Ports        │  │
│  └──────────────┘    └──────────────┘  │
└─────────────────────────────────────────┘
              │              │
              ▼              ▼
┌─────────────────────────────────────────┐
│      Application Layer (애플리케이션)    │
│         - UseCase 구현                   │
└─────────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────────────┐
│         Domain Layer (도메인)            │
│  - Entity, ValueObject, DomainService   │
└─────────────────────────────────────────┘
```

## 패키지 구조

```
com.onuldo/
├── domain/              # 도메인 계층
│   ├── entity/         # 도메인 엔티티
│   ├── valueobject/    # 값 객체
│   └── service/        # 도메인 서비스
│
├── port/               # 포트 계층
│   ├── in/             # 인바운드 포트 (UseCase 인터페이스)
│   └── out/            # 아웃바운드 포트 (Repository 인터페이스)
│
├── application/        # 애플리케이션 계층
│   ├── usecase/        # 유스케이스 구현
│   └── dto/            # 애플리케이션 DTO
│
├── adapter/            # 어댑터 계층
│   ├── in/             # 인바운드 어댑터
│   │   ├── web/        # REST API 컨트롤러
│   │   └── event/      # 이벤트 리스너
│   └── out/            # 아웃바운드 어댑터
│       ├── persistence/ # JPA 리포지토리 구현
│       └── external/   # 외부 API 클라이언트
│
└── common/             # 공통 모듈
    ├── config/         # 설정
    ├── dto/            # 공통 DTO
    └── exception/      # 예외 처리
```

## 계층별 설명

### Domain Layer (도메인 계층)

비즈니스 로직의 핵심을 담당합니다.

- **Entity**: 도메인 엔티티 (JPA 엔티티)
- **ValueObject**: 불변 값 객체
- **DomainService**: 도메인 서비스 (복잡한 비즈니스 로직)

**원칙:**
- 외부 의존성을 가지지 않음
- 순수한 비즈니스 로직만 포함
- JPA 어노테이션은 허용하지만, Spring 의존성은 피함

### Port Layer (포트 계층)

인터페이스를 정의합니다.

- **Inbound Ports**: 애플리케이션 서비스 인터페이스 (UseCase)
- **Outbound Ports**: 리포지토리 및 외부 서비스 인터페이스

**원칙:**
- 포트는 인터페이스만 정의
- 구현은 어댑터 계층에서 이루어짐
- 의존성 역전 원칙(DIP)을 따름

### Application Layer (애플리케이션 계층)

유스케이스를 구현합니다.

- **UseCase**: 비즈니스 유스케이스 구현
- **DTO**: 애플리케이션 계층에서 사용하는 DTO

**원칙:**
- 도메인 로직을 조합하여 유스케이스 구현
- 트랜잭션 경계 관리
- 포트를 통해 외부 계층과 통신

### Adapter Layer (어댑터 계층)

외부 시스템과의 통신을 담당합니다.

- **Inbound Adapters**: 
  - `web/`: REST API 컨트롤러
  - `event/`: 이벤트 리스너
- **Outbound Adapters**:
  - `persistence/`: JPA 리포지토리 구현
  - `external/`: 외부 API 클라이언트

**원칙:**
- 어댑터는 포트 인터페이스를 구현
- 도메인 로직을 포함하지 않음
- 외부 시스템의 변경으로부터 도메인을 보호

### Common (공통 모듈)

프로젝트 전반에서 사용되는 공통 기능입니다.

- **config/**: Spring 설정 클래스
- **dto/**: 공통 응답 DTO (`ApiResponse`)
- **exception/**: 예외 처리 (`GlobalExceptionHandler`, `BusinessException`)

## 의존성 방향

```
Adapter → Port → Application → Domain
  ↓        ↓         ↓          ↑
  └────────┴─────────┴──────────┘
```

- **도메인 계층**: 가장 안쪽, 다른 계층에 의존하지 않음
- **애플리케이션 계층**: 도메인 계층에만 의존
- **포트 계층**: 도메인과 애플리케이션 계층에 의존
- **어댑터 계층**: 포트와 애플리케이션 계층에 의존

## 예시: 타이머 시작 유스케이스

### 1. Domain Layer
```kotlin
// domain/entity/Timer.kt
class Timer : BaseEntity() {
    var userId: Long
    var hobbyId: Long
    var startTime: LocalDateTime
    var status: TimerStatus
}
```

### 2. Port Layer
```kotlin
// port/in/StartTimerUseCase.kt
interface StartTimerUseCase {
    fun execute(command: StartTimerCommand): TimerResult
}

// port/out/TimerRepository.kt
interface TimerRepository {
    fun save(timer: Timer): Timer
    fun findByUserIdAndStatus(userId: Long, status: TimerStatus): Timer?
}
```

### 3. Application Layer
```kotlin
// application/usecase/StartTimerUseCaseImpl.kt
@Service
class StartTimerUseCaseImpl(
    private val timerRepository: TimerRepository
) : StartTimerUseCase {
    override fun execute(command: StartTimerCommand): TimerResult {
        // 비즈니스 로직 구현
    }
}
```

### 4. Adapter Layer
```kotlin
// adapter/in/web/TimerController.kt
@RestController
class TimerController(
    private val startTimerUseCase: StartTimerUseCase
) {
    @PostMapping("/timers")
    fun startTimer(@RequestBody request: StartTimerRequest): ApiResponse<TimerResponse> {
        val result = startTimerUseCase.execute(request.toCommand())
        return ApiResponse.success(result.toResponse())
    }
}

// adapter/out/persistence/TimerJpaRepository.kt
@Repository
interface TimerJpaRepository : JpaRepository<Timer, Long> {
    fun findByUserIdAndStatus(userId: Long, status: TimerStatus): Timer?
}

// adapter/out/persistence/TimerRepositoryImpl.kt
@Repository
class TimerRepositoryImpl(
    private val jpaRepository: TimerJpaRepository
) : TimerRepository {
    override fun save(timer: Timer): Timer = jpaRepository.save(timer)
    override fun findByUserIdAndStatus(userId: Long, status: TimerStatus): Timer? =
        jpaRepository.findByUserIdAndStatus(userId, status)
}
```

## 장점

1. **테스트 용이성**: 각 계층을 독립적으로 테스트 가능
2. **유지보수성**: 비즈니스 로직과 인프라 분리
3. **확장성**: 새로운 어댑터 추가가 용이
4. **도메인 중심**: 비즈니스 로직이 명확하게 드러남

## 참고 자료

- [Hexagonal Architecture (Alistair Cockburn)](https://alistair.cockburn.us/hexagonal-architecture/)
- [Ports and Adapters Pattern](https://martinfowler.com/articles/ports-and-adapters.html)

