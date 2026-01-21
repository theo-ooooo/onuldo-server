# Adapter Layer

어댑터 계층은 외부 시스템과의 통신을 담당합니다.

## 구조

### Inbound Adapters (인바운드 어댑터)

- `in/web/`: REST API 컨트롤러
- `in/event/`: 이벤트 리스너

### Outbound Adapters (아웃바운드 어댑터)

- `out/persistence/`: JPA 리포지토리 구현
- `out/external/`: 외부 API 클라이언트

## 원칙

- 어댑터는 포트 인터페이스를 구현합니다
- 도메인 로직을 포함하지 않습니다
- 외부 시스템의 변경으로부터 도메인을 보호합니다

