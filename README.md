# Food Delivery

**Spring Boot + JPA + MySQL + Redis + JWT + WebSocket** 등을 사용해 구현한 간단한 음식 배달 시스템 예제입니다.  
OAuth2(구글) 로그인까지 연동하여 다양한 인증 시나리오를 실습할 수 있습니다.

<br/>

## 1. 프로젝트 개요
1. **다양한 사용자 권한**(CUSTOMER / OWNER / RIDER / ADMIN) 기반으로 권한별 비즈니스 로직을 직접 체험  

2. **주문-결제-배달** 전 과정을 간단하게나마 시뮬레이션하며 웹 백엔드 전반의 로직을 학습  

3. **Spring Security + JWT** 로 인증/인가 구현, **OAuth2** 를 통한 소셜 로그인(구글) 연동  

4. **Redis** 캐시, **WebSocket(STOMP)** 기반 실시간 위치 업데이트 등 실무에서 자주 사용되는 기술들을 포함  

5. **도메인 설계**(User, Store, Menu, Order, Delivery 등)와 **JPA**를 이용한 DB 연동  

6. **Swagger(SpringDoc)** 로 OpenAPI 문서화  

<br/>

## 2. 주요 기능
1. **회원가입 & 로그인 (JWT / OAuth2)**  
   - Spring Security를 통한 회원가입 & 로그인 로직 (비밀번호는 BCryptPasswordEncoder로 해싱)  
   - 구글 OAuth2 로그인 시도 시, 성공 후 JWT 발급

2. **가게 & 메뉴 관리 (OWNER / ADMIN 권한)**  
   - 가게(Store) 등록/조회  
   - 메뉴(Menu) 등록/조회/수정/삭제

3. **주문 & 결제 (CUSTOMER 권한)**  
   - 주문(Order) 생성 및 결제  
   - 주문 상태 변경(OWNER, ADMIN)

4. **배달 (RIDER 권한)**  
   - 배달(Delivery) 생성, 상태 업데이트  
   - WebSocket을 통해 라이더 위치 실시간 전송, 구독 중인 사용자에게 실시간 배달 상태/위치 정보 Broadcast

5. **Redis 캐싱**  
   - Bean 설정을 통해 Redis를 이용한 캐시 사용 (예: 자주 호출되는 데이터 등)

6. **Swagger(OpenAPI) 문서화**  
   - SpringDoc 사용  
   - 주요 Endpoint를 그룹핑해서 편리하게 테스트 가능

<br/>

## 3. 기술 스택
- **Back-end**  
  - Java 17  

  - Spring Boot 3.x  

  - Spring Data JPA (Hibernate)  

  - Spring Security (JWT, OAuth2 Client)  

  - WebSocket (SockJS + STOMP)  

  - Redis (캐싱)  

   - MySQL  

- **빌드/관리**  
  - Gradle  

- **API 문서화**  
  - SpringDoc (Swagger UI)

<br/>

## 4. 프로젝트 구조
```
food-delivery
 ┣ src
 ┃ ┣ main
 ┃ ┃ ┣ java/com/example/fooddelivery
 ┃ ┃ ┃ ┣ config/           # 보안(Security), CORS, Redis, Swagger, WebSocket 설정 등
 ┃ ┃ ┃ ┣ controller/       # REST API + WebSocket 관련 컨트롤러
 ┃ ┃ ┃ ┣ domain/           # JPA 엔티티 (User, Store, Menu, Order, Delivery 등)
 ┃ ┃ ┃ ┣ dto/              # DTO 클래스
 ┃ ┃ ┃ ┣ exception/        # 전역 예외 처리
 ┃ ┃ ┃ ┣ repository/       # JPA 레포지토리 인터페이스
 ┃ ┃ ┃ ┣ security/         # JWT 인증 필터, Provider
 ┃ ┃ ┃ ┗ service/          # 비즈니스 로직 (UserService, OrderService, DeliveryService 등)
 ┃ ┃ ┗ resources
 ┃ ┃    ┗ application.yml  # DB, Redis, OAuth2 설정, 로깅 설정
 ┃ ┗ test
 ┃    ┗ java/com/example/fooddelivery
 ┗ build.gradle
 ```
<br>

## 5. 사용 방법

### 5.1 로컬 환경에서 실행하기

1. **MySQL** 실행 후, `food_delivery_db` 데이터베이스 생성 (또는 `application.yml`에 맞춰 DB 설정)

2. **Redis** 실행 (혹은 로컬 설치)

3. **프로젝트 클론**
```
git clone https://github.com/your-repo/food-delivery.git
```

4. **Gradle 빌드 & 실행**
```
./gradlew bootRun
```

5. 서버 구동 후 `http://localhost:8080` 에서 정상 동작 확인

<br/>

### 5.2 Swagger(OpenAPI)로 API 확인
- **Swagger UI** 접속 URL (기본값):
```
http://localhost:8080/swagger-ui/index.html
```
- 주요 Endpoints:

  - **회원가입:** `POST /api/v1/auth/signup`

  - **로그인:** `POST /api/v1/auth/login` (JWT 토큰 반환)

  - **메뉴 조회:** `GET /api/v1/menu/{storeId}`

  - **주문 생성:** `POST /api/v1/orders` (헤더에 Authorization: Bearer {토큰})

  - **배달 생성:** `POST /api/v1/delivery/{orderId}` (RIDER 권한 필요)

<br/>

## 6. 시연 예시

1. **회원 가입**
```
POST /api/v1/auth/signup
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "1234",
  "name": "Tester",
  "phone": "010-1234-5678"

}
```
2. 로그인 → 응답으로 JWT 토큰을 수령
```
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "1234"
}
```
3. 가게 생성 (OWNER 또는 ADMIN 권한)
```
POST /api/v1/store
Authorization: Bearer {JWT}
Content-Type: application/json

{
  "storeName": "My Store",
  "address": "서울시 강남구",
  "phone": "02-1234-5678",
  "ownerEmail": "owner@example.com"
}
```
4. 메뉴 등록 (OWNER)
```
POST /api/v1/menu
Authorization: Bearer {JWT}
Content-Type: application/json

{
  "storeId": 1,
  "name": "Spicy Chicken",
  "price": 15000,
  "soldOut": false
}
```
5. 주문 생성 (CUSTOMER)
```
POST /api/v1/orders
Authorization: Bearer {JWT}
Content-Type: application/json

{
  "menuIds": [ 1, 2 ],
  "quantities": [ 2, 1 ],
  "address": "서울시 서초구",
  "paymentMethod": "CARD"
}
```
6. 배달 생성 (RIDER)
```
POST /api/v1/delivery/{orderId}
Authorization: Bearer {JWT}
```
<br/>

## 7. 주요 학습 포인트
1. **Spring Security / OAuth2**  

   - JWT 토큰 발급 & 검증 로직  

   - Google OAuth2 연동 후 `CustomOAuth2SuccessHandler`에서 JWT 발급

2. **JPA / DB**  

   - User, Store, Menu, Order, Delivery 등의 엔티티 설계  

   - 다대일, 일대다 매핑 및 트랜잭션 처리

3. **Redis**  

   - 캐시 매니저 설정, `GenericJackson2JsonRedisSerializer` 활용  

   - DB 부하를 줄이기 위한 캐싱 전략

4. **WebSocket(STOMP)**  

   - 라이더의 실시간 위치 업데이트 시나리오 구현 (ChannelInterceptor로 JWT 인증 처리)  

   - `/topic/**` 브로커를 통해 메시지 Broadcast

5. **로깅 & 예외 처리**  

   - `GlobalExceptionHandler`를 통한 전역 예외 처리  

   - 로그 레벨 설정을 통해 Hibernate SQL 로그 모니터링

6. **역할(Role) 기반 접근 제어**  
   - `@PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")` 같은 메서드 수준 보안 적용
