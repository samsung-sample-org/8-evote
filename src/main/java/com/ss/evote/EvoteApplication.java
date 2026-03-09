package com.ss.evote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 전자투표 시스템 애플리케이션 진입점.
 *
 * <p>본 프로젝트는 JDK 17 + Spring Boot 3.5.11 환경에서
 * 기존(ASIS) 라이브러리들의 TOBE 전환 호환성을 검증하기 위한 샘플 애플리케이션이다.</p>
 *
 * <p>6-company-intro와의 주요 차이점:</p>
 * <ul>
 *   <li><b>JPA(Hibernate) 대신 MyBatis 사용</b> - mybatis-spring-boot-starter 3.0.3</li>
 *   <li>솔루션 라이브러리 약 20건이 리스크 포인트 (벤더 협력 필요)</li>
 * </ul>
 *
 * <p>검증 대상 라이브러리:</p>
 * <ul>
 *   <li>MyBatis 3.0.3 (mybatis-spring-boot-starter, JPA 대신 사용)</li>
 *   <li>Ehcache 3.10.8 (javax 기반 Ehcache 2.x → JCache 기반 전환)</li>
 *   <li>Apache HttpClient 5 (4.x → 5.x, groupId 완전 변경)</li>
 *   <li>Quartz Scheduler (1.x → spring-boot-starter-quartz)</li>
 *   <li>commons-collections4 (3.x RCE CVE-2015-6420 패치 필수)</li>
 *   <li>antisamy 1.7.6 (lucy-xss 대체)</li>
 *   <li>Velocity 2.3, dom4j 2.1.4, Xerces 2.12.2, POI 5.2.5 등</li>
 * </ul>
 *
 * @author SS Sample
 */
@SpringBootApplication
public class EvoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvoteApplication.class, args);
    }
}
