# 8. 전자투표 시스템 - JDK 17 마이그레이션 라이브러리 검증

## 목차

1. [개요](#1-개요)
2. [기술 스택](#2-기술-스택)
3. [라이브러리 전환 현황 (ASIS → TOBE)](#3-라이브러리-전환-현황-asis--tobe)
   - [전환 원칙](#전환-원칙)
   - [전환 요약](#전환-요약)
   - [전체 라이브러리 매트릭스](#전체-라이브러리-매트릭스)
4. [솔루션/자체 라이브러리 (주요 리스크)](#4-솔루션자체-라이브러리-주요-리스크)
5. [MyBatis 전환 특이사항](#5-mybatis-전환-특이사항)
6. [프로젝트 구조](#6-프로젝트-구조)
7. [실행 방법](#7-실행-방법)
8. [테스트 실행](#8-테스트-실행)
9. [알려진 제약사항](#9-알려진-제약사항)

---

## 1. 개요

기존 전자투표 시스템(Spring Framework 3.x, JDK 8)을 JDK 17 + Spring Boot 3.5.11로 전환하기 위한 **라이브러리 호환성 검증 프로젝트**이다.

**6-company-intro와의 주요 차이점:**
- **JPA(Hibernate) 대신 MyBatis 사용** — mybatis-spring-boot-starter 3.0.3
- **솔루션/자체 라이브러리 약 20건** — 벤더 협력 필수, 주요 리스크 포인트

---

## 2. 기술 스택

| 항목 | 선택 | 선택 이유 |
|------|------|-----------|
| JDK | 17 (Adoptium Temurin) | Spring Boot 3의 최소 요구사항. LTS 버전으로 장기 지원 보장 |
| Framework | Spring Boot 3.5.11 | Jakarta EE 10 기반, Spring Framework 6.1 내장 |
| 빌드 도구 | Maven | 기존 시스템과 동일한 빌드 도구 유지 |
| ORM | **MyBatis 3.0.3** | 기존 시스템이 MyBatis 기반. JPA 대신 MyBatis 유지 |
| 패키징 | **JAR** | JSP 없음, 내장 톰캣으로 직접 실행 |
| 컨테이너 OS | Docker (CentOS 7) | 레거시 환경 재현. 운영 시 Rocky Linux 9 권장 |
| DB (로컬) | H2 인메모리 | MyBatis 테스트용 인메모리 DB |

---

## 3. 라이브러리 전환 현황 (ASIS → TOBE)

### 전환 원칙

1. **기존 기술 최대 유지**: 있던 라이브러리를 그대로 가져간다.
2. **버전업 우선**: 동일 라이브러리의 최신 버전으로 업그레이드를 1순위로 한다.
3. **교체는 불가피한 경우만**: 버전업만으로 대응할 수 없을 때만 대체 라이브러리를 고려한다.
4. **MyBatis 유지**: 기존 시스템이 MyBatis 기반이므로 JPA로 전환하지 않는다.

### 전환 요약

| 전환 방식 | 건수 | 설명 |
|----------|------|------|
| Boot 내장 | 6건 | Spring Boot Starter/BOM에 포함. 별도 의존성 관리 불필요 |
| 버전업 | 14건 | 동일 계열 라이브러리 최신 버전으로 업그레이드 |
| 교체 | 2건 | 버전업 불가, 대체 라이브러리로 전환 (불가피) |
| 제거 | 5건 | EOL/보안취약점/Jakarta 미호환으로 제거 |
| 솔루션/자체 | **20건** | **벤더 협력 필요 — 주요 리스크** |

### 전체 라이브러리 매트릭스

#### Framework / Core

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 1 | Spring Framework 3.x | Spring 6.1+ | Boot 내장 | starter-web. javax→jakarta 전환 |
| 2 | Hibernate Validator 4.1 | Validator 8.x | Boot 내장 | starter-validation. javax.validation→jakarta.validation |
| 3 | aopalliance + AspectJ 1.6.x | AspectJ 1.9.21+ | Boot 내장 | starter-aop |
| 4 | Jackson 1.x (org.codehaus) | Jackson 2.17+ | Boot 내장 | starter-web. 패키지 완전 변경 |

#### ORM / DB (핵심 차이점)

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 5 | mybatis 3.2.x | **mybatis-spring-boot-starter 3.0.3** | 버전업 | **JPA 대신 MyBatis 유지** |

#### Cache

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 6 | ehcache 2.x (javax) | ehcache 3.10.8 (jakarta) | 버전업 | **classifier=jakarta 필수**. JCache(JSR-107) 표준 |

#### Scheduler

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 7 | quartz 1.x | Quartz 2.5+ | 버전업 | starter-quartz. Boot 자동 구성 |

#### HTTP

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 8 | httpclient 4.x + commons-httpclient 3.x | httpclient5 | 버전업+제거 | groupId 완전 변경. commons-httpclient 3.x EOL 제거 |

#### Security

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 9 | antisamy 1.4.4 | antisamy 1.7.6 | 버전업 | XSS 필터. **lucy-xss 대체** |
| 10 | lucy-xss | (제거) | 제거 | 유지보수 중단, Jakarta 미대응. antisamy로 대체 |

#### Office

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 11 | poi 3.x | poi 5.2.5 + poi-ooxml 5.2.5 | 버전업 | .xlsx OOXML 지원 추가. XXE 취약점 패치 |

#### XML

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 12 | dom4j 1.x (dom4j:dom4j) | dom4j 2.1.4 (org.dom4j) | 버전업 | groupId 변경. XXE 취약점 패치 |
| 13 | xercesImpl 2.x | xercesImpl 2.12.2 | 버전업 | XML 파싱 보안 패치 |

#### Template

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 14 | velocity 1.x | velocity-engine-core 2.3 | 버전업 | artifactId 변경. 수동 Bean 설정 필요 |

#### Apache Commons

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 15 | commons-beanutils 1.8.x | commons-beanutils 1.9.4 | 버전업 | CVE-2014-0114 패치 |
| 16 | commons-codec 1.x | commons-codec (Boot 관리) | Boot 내장 | Boot BOM 버전 관리 |
| 17 | commons-collections 3.x | **commons-collections4 4.4** | 교체 | **RCE 취약점(CVE-2015-6420) — 필수 전환** |
| 18 | commons-compress 1.x | commons-compress 1.26.1 | 버전업 | Zip Bomb 방어 강화 |
| 19 | commons-io 1.x | commons-io 2.16.1 | 버전업 | JDK 8+ NIO 지원 |
| 20 | commons-lang 2.x | commons-lang3 (Boot 관리) | Boot 내장 | 패키지 변경(lang → lang3) |
| 21 | commons-pool 1.x | commons-pool2 (Boot 관리) | Boot 내장 | artifactId 변경 |

#### Utility

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 22 | joda-time 1.x/2.x | joda-time 2.12.7 | 버전업 | 기존 코드 유지. java.time 전환 점진적 권장 |
| 23 | json-simple 구버전 | json-simple 1.1.1 | 버전업 | 기존 코드 유지. 신규 코드는 Gson 권장 |
| 24 | (없음) | Gson 2.10.1 | 신규 | json-simple 대체 권장 라이브러리 |
| 25 | nekohtml 1.x | jsoup 1.18.1 | 교체 | NekoHTML EOL. Jsoup으로 대체 |
| 26 | activation 1.1 (javax) | jakarta.activation-api (Boot 관리) | Boot 내장 | JDK 17에서 javax.activation 제거 |

#### Logging

| # | ASIS | TOBE | 방식 | 비고 |
|---|------|------|------|------|
| 27 | log4j 1.x | log4j-1.2-api + log4j-core (Boot 관리) | 교체 | CVE-2019-17571. 기존 API 유지하며 Logback으로 라우팅 |

#### 제거 목록

| # | ASIS | 제거 사유 |
|---|------|----------|
| R1 | lucy-xss | 유지보수 중단, Jakarta 미대응 → antisamy로 대체 |
| R2 | commons-httpclient 3.x | EOL → httpclient5로 통합 |
| R3 | commons-collections 3.x | CVE-2015-6420 RCE 취약점 → collections4로 필수 전환 |
| R4 | ehcache 2.x | javax 기반, Jakarta 비호환 → ehcache3로 전환 |
| R5 | jackson 1.x | Boot Jackson2와 별개, 불필요 |

---

## 4. 솔루션/자체 라이브러리 (주요 리스크)

> **이 섹션은 8-evote의 가장 중요한 리스크 포인트이다.**
> 바이너리가 Maven Central에 배포되어 있지 않거나, 소스 코드를 확보할 수 없는 라이브러리로
> **각 벤더/담당 부서에 JDK 17 호환 여부를 개별 확인해야 한다.**

| 파일명 | 추정 용도 | 담당 | 향후 조치 |
|--------|----------|------|----------|
| af_auth_v1.0.0.3.jar | 인증 모듈 | 내부 | JDK 17 호환 버전 요청 |
| astx2_1.3.0.21.jar | ASTX 암호화 | 벤더 | JDK 17 호환 확인 |
| bio.jar | 공통 라이브러리 | 내부 | JDK 17 호환 버전 요청 |
| cos.jar | 구형 파일업로드 | (대체 권장) | Spring Boot Multipart로 대체 검토 |
| Genesys_*.jar | Genesys 연계 | 벤더(Genesys) | JDK 17 / Jakarta EE 호환 문의 |
| httpClient.jar | 커스텀 HTTP 클라이언트 | 내부 | httpclient5로 교체 검토 |
| ICERTSecu_JDK16.jar | 인증서 보안 | 벤더 | JDK 17 호환 버전 요청 (JDK16 전용 주의) |
| jconn3.jar | Sybase JDBC | 벤더(SAP) | SAP에 JDK 17 호환 드라이버 확인 |
| KmcCrypto.jar | 암호화 | 벤더(KMC) | JDK 17 호환 버전 요청 |
| ocrserver-2.4.0.jar | OCR 처리 | 벤더 | JDK 17 호환 확인 |
| ozenc_utf8.jar | 인코딩/보안 모듈 | 담당 부서 | JDK 17 호환 버전 요청 |
| queryapi500.jar | 쿼리 API | 담당 부서 | 업데이트 버전 요청 |
| RpsMerge.jar | 리포트 병합 | 벤더 | JDK 17 호환 확인 |
| rptcertapi.jar | 리포트 인증 | 벤더 | JDK 17 호환 확인 |
| sciSecu_v2.jar / sciSecuPCC.jar | 보안 모듈 | 보안팀 | JDK 17 호환 확인 |
| secui.jar | 보안 솔루션 | 벤더(secui) | JDK 17 / Jakarta EE 호환 문의 |
| seedx.jar | SEED 암호화 | 암호화팀 | JDK 17 호환 테스트 필요 |
| spring-json-1.2.jar | 구 Spring JSON | (대체 완료) | Boot Jackson 2.x로 대체됨 |
| ss-common.jar / ss-security.jar | 내부 공통/보안 모듈 | 내부 | JDK 17 호환 버전 빌드 필요 |
| transkey-4-6-12_16_20190403_X.jar | 키보드 보안 | 벤더(raon secure) | JDK 17 / Jakarta EE 호환 문의 |

**총 약 20건 — 벤더 협력 없이는 전환 불가능한 항목들이다.**

---

## 5. MyBatis 전환 특이사항

### 6-company-intro(JPA)와의 차이점

| 항목 | 6-company-intro | 8-evote |
|------|----------------|---------|
| ORM | spring-boot-starter-data-jpa (Hibernate 6) | **mybatis-spring-boot-starter 3.0.3** |
| 패키징 | WAR (JSP 지원) | **JAR** |
| 엔티티 | @Entity, @Repository | **@Mapper, XML Mapper** |
| DB 스키마 | ddl-auto: create-drop (자동) | **수동 스키마 관리** |
| 테스트 | @SpringBootTest + JpaTest | **@MybatisTest 슬라이스 테스트** |

### MyBatis 3.0.3 전환 포인트

1. **SqlSessionFactory 자동 구성**: mybatis-spring-boot-starter가 SqlSessionFactory와 SqlSessionTemplate을 자동 등록한다.
2. **@MapperScan**: `MyBatisConfig.java`의 `@MapperScan("com.ss.evote.mapper")`로 Mapper 인터페이스를 자동 등록한다.
3. **XML Mapper**: `application.yml`의 `mybatis.mapper-locations: classpath:mapper/*.xml`로 XML Mapper를 지정한다.
4. **언더스코어 → 카멜케이스**: `map-underscore-to-camel-case: true`로 자동 변환을 활성화한다.
5. **@MybatisTest**: H2 인메모리 DB로 자동 구성되는 슬라이스 테스트로 Mapper 계층만 격리 테스트한다.

---

## 6. 프로젝트 구조

```
8-evote/
├── Dockerfile                              # Multi-stage 빌드 (Maven → CentOS 7 + JDK 17)
├── docker-compose.yml                      # evote-app 컨테이너
├── pom.xml                                 # 전체 의존성 정의 (ASIS→TOBE 주석 포함)
├── README.md
└── src/
    ├── main/
    │   ├── java/com/ss/evote/
    │   │   ├── EvoteApplication.java
    │   │   ├── config/
    │   │   │   ├── CacheConfig.java        # Ehcache 3 JCache
    │   │   │   ├── HttpClientConfig.java   # HttpClient 5 Bean
    │   │   │   ├── MyBatisConfig.java      # MyBatis @MapperScan 설정
    │   │   │   └── QuartzConfig.java       # Quartz 스케줄러
    │   │   ├── controller/
    │   │   │   └── HealthCheckController.java
    │   │   ├── job/
    │   │   │   └── SampleQuartzJob.java
    │   │   └── mapper/
    │   │       └── SampleMapper.java       # MyBatis Mapper 인터페이스
    │   └── resources/
    │       ├── application.yml             # H2 + MyBatis + JCache 설정 (포트 8083)
    │       ├── ehcache.xml                 # Ehcache 3
    │       ├── logback-spring.xml
    │       └── mapper/
    │           └── SampleMapper.xml        # MyBatis XML Mapper (SELECT 1 쿼리)
    └── test/java/com/ss/evote/
        ├── boot/
        │   ├── BootContextTest.java        # Spring Boot 3.5.11 컨텍스트 로딩
        │   └── MyBatisTest.java            # MyBatis 3.0.3 매퍼 동작 (핵심!)
        ├── cache/                          # Ehcache3Test.java
        ├── commons/                        # CollectionsTest, IoTest, Lang3Test, BeanutilsTest, CompressTest
        ├── http/                           # HttpClient5Test.java
        ├── office/                         # PoiTest.java
        ├── removed/                        # RemovedLibDocTest.java
        ├── scheduler/                      # QuartzTest.java
        ├── security/                       # AntiSamyTest.java
        ├── template/                       # VelocityTest.java
        ├── util/                           # JodaTimeTest, JsonTest, JsoupTest
        └── xml/                            # Dom4jTest, XercesTest
```

---

## 7. 실행 방법

### 로컬 실행 (H2 인메모리 DB)

```bash
cd 8-evote
mvn spring-boot:run
```

- 애플리케이션: http://localhost:8083
- H2 콘솔: http://localhost:8083/h2-console (JDBC URL: `jdbc:h2:mem:evotedb`)

### Docker 실행

```bash
cd 8-evote
docker-compose up -d
```

- 애플리케이션: http://localhost:8083

---

## 8. 테스트 실행

```bash
cd 8-evote
mvn clean test
```

### 테스트 그룹별 검증 대상

| 테스트 그룹 | 검증 대상 라이브러리 |
|------------|-------------------|
| boot/ | Spring Boot 3.5.11 컨텍스트 로딩, **MyBatis 3.0.3 매퍼 동작 (핵심)** |
| cache/ | Ehcache 3.10.8 (JCache, jakarta classifier) |
| commons/ | beanutils 1.9.4, collections4 4.4, compress 1.26.1, io 2.16.1, lang3 |
| http/ | HttpClient 5 (클라이언트 생성, 요청 객체) |
| office/ | Apache POI 5.2.5 (Excel .xlsx 읽기/쓰기) |
| removed/ | 제거 라이브러리 ClassPath 부재 확인 (lucy-xss, commons-collections 3.x 등) |
| scheduler/ | Quartz 2.5+ (Boot 자동 구성) |
| security/ | AntiSamy 1.7.6 (XSS 필터, lucy-xss 대체 확인) |
| template/ | Velocity 2.3 |
| util/ | joda-time 2.12.7, Gson 2.10.1, json-simple 1.1.1, jsoup 1.18.1 |
| xml/ | dom4j 2.1.4, Xerces 2.12.2 |

---

## 9. 알려진 제약사항

1. **솔루션/자체 라이브러리 미검증 (~20건)**: af_auth, astx2, bio, Genesys, ICERTSecu, jconn3, KmcCrypto 등은 바이너리 미확보로 본 프로젝트에 포함되지 않았다. 각 벤더/담당 부서에 JDK 17 호환 버전을 별도 요청해야 한다.

2. **commons-collections 3.x → 4.x 전환 시 코드 수정 필요**: 패키지명이 `org.apache.commons.collections` → `org.apache.commons.collections4`로 변경되어 모든 import를 수정해야 한다. CVE-2015-6420 RCE 취약점으로 전환이 필수이다.

3. **Sybase JDBC (jconn3.jar)**: SAP에서 JDK 17 호환 드라이버를 제공하는지 확인이 필요하다. H2를 로컬 테스트 DB로 사용한다.

4. **ICERTSecu_JDK16.jar**: 파일명에서 확인되듯 JDK 16 전용으로 개발된 것으로 추정된다. JDK 17 호환 여부를 반드시 확인해야 한다.

5. **cos.jar (파일업로드)**: 구형 라이브러리로 Spring Boot의 `StandardServletMultipartResolver`로 대체를 검토한다.

6. **spring-json-1.2.jar**: Spring Framework 구버전용 JSON 라이브러리로 Boot Jackson 2.x로 대체 완료로 간주한다.
