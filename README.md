# Spring Boot 게시판 프로젝트

이 프로젝트는 Spring Boot, Gradle, MySQL을 사용하여 개발한 간단한 웹 게시판입니다.
<img width="1710" height="788" alt="Image" src="https://github.com/user-attachments/assets/711b2f9f-8fbc-435e-abc8-d5a25461426d" />

## 주요 기능
- 회원가입 및 로그인 기능
- 게시글 CRUD (생성, 조회, 수정, 삭제)
- 사용자 정보 조회 및 관리

## 기술 스택
- **Backend**: Java 17, Spring Boot
- **Database**: MySQL
- **Build Tool**: Gradle
- **Template Engine**: Thymeleaf

## 프로젝트 구조
```
demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pratice/demo/
│   │   │       ├── DemoApplication.java  # 메인 애플리케이션
│   │   │       ├── config/             # Spring Security, 비밀번호 인코더 등 설정
│   │   │       ├── controller/         # HTTP 요청 처리 컨트롤러
│   │   │       ├── dto/                # 데이터 전송 객체 (Request/Response)
│   │   │       ├── model/              # JPA 엔티티 (데이터베이스 테이블)
│   │   │       ├── repository/         # JPA 리포지토리 (데이터베이스 접근)
│   │   │       └── service/            # 비즈니스 로직
│   │   └── resources/
│   │       ├── static/                 # CSS, JS, 이미지 등 정적 파일
│   │       ├── templates/              # Thymeleaf HTML 템플릿
│   │       └── application.properties  # 애플리케이션 설정
│   └── test/                           # 테스트 코드
├── build.gradle                        # Gradle 빌드 스크립트
└── README.md                           # 프로젝트 설명 파일
```

## ERD (Entity Relationship Diagram)

```mermaid
erDiagram
    MEMBER {
        Long id PK "회원 ID"
        String username "로그인 ID"
        String password "비밀번호"
        String name "이름"
    }

    CONTENT {
        Long id PK "게시글 ID"
        String title "제목"
        String body "내용"
        Long member_id FK "작성자 ID"
        datetime createdAt "생성일"
    }

    MEMBER ||--o{ CONTENT : "writes"
  ```

## 시작하기

### 1. 데이터베이스 설정
로컬 환경에 MySQL 데이터베이스를 설치하고, `demo` 스키마를 생성합니다.

### 2. 애플리케이션 설정
`src/main/resources/application.properties` 파일에 자신의 데이터베이스 정보를 입력합니다.

```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/demo?serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=
```
### 3. 실행 확인
브라우저에서 localhost:8080에 접속합니다.
