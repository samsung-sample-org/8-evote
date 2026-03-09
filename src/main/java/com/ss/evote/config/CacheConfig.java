package com.ss.evote.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 캐시 설정.
 *
 * <p>ASIS: Ehcache 2.x (javax 기반, ehcache.xml 직접 설정)<br>
 * TOBE: Ehcache 3.10.8 (jakarta 호환, JCache(JSR-107) 표준 기반)</p>
 *
 * <p>전환 이유:</p>
 * <ul>
 *   <li>Ehcache 2.x는 javax.cache(JSR-107)를 부분 지원하며, Spring Boot 3과의 호환성이 보장되지 않는다.</li>
 *   <li>Ehcache 3.x는 JCache(JSR-107) 표준을 완전 구현하며, jakarta 네임스페이스와 호환된다.</li>
 *   <li>Spring Boot 3의 spring.cache.jcache.config 속성을 통해
 *       application.yml에서 JCache 설정 파일 경로를 지정하여 캐시를 구성한다.</li>
 * </ul>
 *
 * <pre>
 * # application.yml 예시
 * spring:
 *   cache:
 *     type: jcache
 *     jcache:
 *       config: classpath:ehcache.xml
 * </pre>
 */
@Configuration
@EnableCaching
public class CacheConfig {
    // JCache 설정은 application.yml의 spring.cache.jcache.config로 처리하므로
    // 별도 Bean 정의가 필요하지 않다.
}
