# 🌌 미리내 (Mirinae)
> **공간 구독형 실시간 잔상(Residual Star) 플랫폼** > 3D 가상 지구 위에서 특정 지역의 공간적 이벤트를 구독하고, 시간의 흐름에 따라 소멸하는 감성적 위치 기반 흔적을 실시간으로 감지하는 SNS 서비스입니다.

본 프로젝트는 백엔드와 프론트엔드가 공존하는 **모노레포(Monorepo)** 구조로 관리되며, 거시적 구조 설계를 위한 **DDD(도메인 주도 설계)**와 미시적 코드 품질 확보를 위한 **TDD(테스트 주도 개발)**를 결합하여 아키텍처적 완성도를 검증하는 것을 목표로 합니다.

---

## 🚀 1. 핵심 아키텍처 및 데이터 흐름 (Data Flow)

'미리내' 시스템은 사용자 간의 직접적인 일대일 소통 대신, **"공간 연산 기반의 메시지 중계 및 단방향 실시간 푸시"** 모델을 취합니다. 백엔드 다중화(Scale-out) 상황에서도 안정적으로 신호를 라우팅할 수 있는 분산 구조를 지향합니다.

---

## 🛠 2. 기술 스택 (Tech Stacks)

### Backend
- **Language/Framework**: Java 25, Spring Boot 4.0.x
- **Data/Spatial**: PostgreSQL, PostGIS
- **Cache/Messaging**: Redis (Pub/Sub & Key-space Notifications)
- **Security**: Spring Security, OAuth2 Client, OWASP A01 Data Projection

### Frontend
- **Core**: React, Vite
- **3D Graphics**: Three.js, React Three Fiber (R3F)
- **Styling**: Tailwind CSS

### Infrastructure & CI/CD
- **OS**: Rocky Linux
- **Web Server**: Nginx
- **Automation**: GitHub Actions (CI/CD Pipeline)

---

## 🏗 3. 핵심 개발 방법론 및 설계 원칙

### 💡 Domain-Driven Design (DDD)
- **순수 도메인 레이어 격리**: 데이터베이스(Postgres) 및 메시지 큐(Redis) 등의 인프라 기술에 종속되지 않는 순수 자바 객체(POJO) 형태의 도메인 모델을 구축했습니다.
- **풍부한 도메인 모델(Rich Domain Model)**: 잔별(Janbyeol)의 수명 변동 로직(로그 함수 기반 연장 공식 및 한계선 제어)을 서비스 레이어가 아닌 엔티티 내부 메서드에 응집시켜 객체 지향적 책임을 강화했습니다.

### 🧪 Test-Driven Development (TDD)
- **Red-Green-Refactor 생명주기 준수**: 무거운 인프라 환경 모킹 없이 비즈니스 명세를 빠르게 검증할 수 있도록 도메인 엔티티의 단위 테스트를 완벽히 선행(Test-First)하여 구현했습니다.
- **회귀 테스트 기반 리팩토링**: 촘촘히 작성된 단위 테스트 라인을 기반으로 코드의 가독성과 성능을 안전하게 개선하는 루틴을 확립했습니다.

---

## ⚡ 4. 주요 기술적 도전 및 최적화 포인트 (Key Challenges)

### ① PostGIS 공간 인덱스(GiST)를 통한 지오펜싱 병목 해결
- **문제**: 잔별 등록 시 활성화된 모든 두리터(구독 반경)와의 거리 연산($O(N)$)으로 인한 DB CPU 부하 우려
- **해결**: PostGIS의 테이블 내 geometry 컬럼에 **GiST(Inverted File Index 기반 공간 인덱스)**를 바인딩하여 트리 탐색 구조로 쿼리 연산 속도를 최적화했습니다.

### ② Redis Pub/Sub 기반 분산 서버 알림 동기화
- **문제**: Scale-out 환경에서 사용자가 접속한 서버 인스턴스가 다를 경우 실시간 알림 라우팅 실패 리스크 발생
- **해결**: 인메모리 영역의 **Redis Pub/Sub**을 공통 메시지 브로커로 채택하여, 개별 서버 스레드 고갈 없이 전 세계 노드로 공간 이벤트를 초고속 방송(Broadcasting)하는 라우팅 구조를 구현했습니다.

### ③ Redis Key-space Notifications를 활용한 Dynamic TTL 동기화
- **문제**: 10분 뒤 자동 휘발 및 리액션별 실시간 수명 연장 처리를 위해 RDB를 주기적으로 폴링(Polling)하는 방식의 디스크 I/O 병목
- **해결**: 잔별의 고유 식별자와 만료 시간을 **Redis Cache**에 동시 적재하고, 키 만료(`EXPIRED`) 이벤트를 백엔드가 비동기 리스닝하여 DB 레코드를 처리하는 이벤트 기반 아키텍처를 구축했습니다.

### ④ R3F InstancedMesh를 통한 3D 지구본 렌더링 최적화
- **문제**: 가상 가우시안 캔버스(누리) 위에 수천 개의 잔별 마커를 독립적인 `<mesh>` 컴포넌트로 생성 시 GPU Draw Call 폭증 및 프레임 드랍 발생
- **해결**: WebGL 최적화를 위해 Three.js의 **`InstancedMesh`** 구조를 채택하여 단 한 번의 Draw Call로 수많은 가상 별무리 객체를 일괄 렌더링하도록 프론트엔드 파이프라인을 최적화했습니다.

---

## 🛡 5. 보안 및 데이터 거버넌스 (Security First)

1. **Secret 자산 격리 아키텍처**: 개발 초기 단계부터 `.gitignore` 마스터 규칙을 통해 모든 인증 키 및 환경 설정을 코드베이스와 원천 격리했습니다. 로컬 환경은 암호화된 파일 로드를 사용하며, 운영 배포 환경(Rocky Linux)은 **GitHub Secrets**와 **OS 시스템 환경 변수**를 통해 런타임에 동적 주입됩니다.
2. **타국가 데이터 가시성 제어 (OWASP A01 대응)**: 글로벌 가시성 제한 규칙에 의거, 타 영역의 잔별 정보를 조회할 때는 물리적으로 분리된 `JanbyeolSummaryDTO`로 프로젝션(Projection)하여 가시 영역 외 세부 정보가 네트워크 단에서 유출되는 취약점(Broken Access Control)을 원천 차단했습니다.

---

## 📂 6. 프로젝트 디렉토리 구조 (Directory Structure)

```text
mirinae/
├── .gitignore               # 프로젝트 마스터 Git 제외 규칙 파일
├── docs/                    # 프로젝트 문서 관리 디렉토리
│   └── CONVENTION.md        # DDD/TDD 및 브랜치/커밋 컨벤션 가이드라인
├── backend/                 # Spring Boot 기반 자바 백엔드 모듈 (Gradle Module)
│   ├── build.gradle.kts     # Hibernate Spatial, Redis 종속성 포함 빌드 스크립트
│   └── src/                 # 순수 도메인 레이어 중심의 백엔드 소스 코드
└── frontend/                # React / Vite / R3F 기반 웹 프론트엔드 영역
    ├── package.json         # Node.js 패키지 및 3D 그래픽스 의존성 설정
    └── src/                 # 가상 지구본(누리) 인터랙션 렌더링 소스 코드
