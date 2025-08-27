# boot-jpa-base

## 프로젝트 개요

이 프로젝트는 Spring Boot, JPA, QueryDSL을 활용한 백엔드 애플리케이션입니다. 데이터베이스로 MariaDB를 사용하며, API 문서를 위해 Swagger(SpringDoc)를 적용하였습니다.

## 기술 스택

- **언어:** Java 17
- **프레임워크:** Spring Boot 3.2.4
- **빌드 도구:** Gradle
- **데이터베이스:** MariaDB
- **ORM:** Spring Data JPA
- **쿼리 빌더:** QueryDSL 5.0.0
- **로깅:** P6Spy
- **문서화:** Swagger (SpringDoc OpenAPI)

## 프로젝트 설정

### 1. 환경 설정

- **Java 17** 이상이 필요합니다.

### 2. 데이터베이스 설정

`application.yml`에서 MariaDB 설정을 해야 합니다.

```properties
spring:
    datasource:
        url: jdbc:mariadb://localhost:3306/your_database
        driver-class-name: org.mariadb.jdbc.Driver
        username: your_username
        password: your_password
```

## 프로젝트 의존성

`build.gradle`에서 주요 의존성 목록:

```gradle
// Spring Boot starters
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-validation'

// Swagger
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'

// Lombok
annotationProcessor 'org.projectlombok:lombok'
compileOnly 'org.projectlombok:lombok'

// MariaDB
runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'

// QueryDSL
implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
annotationProcessor 'com.querydsl:querydsl-apt:5.0.0:jakarta'
```

## 주요 기능

- JPA를 활용한 데이터베이스 관리
- QueryDSL을 통한 동적 쿼리 생성
- Swagger UI를 통한 API 문서화
- P6Spy를 통한 SQL 로깅 및 모니터링

## API 문서 확인

애플리케이션 실행 후, Swagger UI를 통해 API 문서를 확인할 수 있습니다.

- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)