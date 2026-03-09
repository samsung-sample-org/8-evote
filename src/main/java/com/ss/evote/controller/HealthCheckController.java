package com.ss.evote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 헬스체크 컨트롤러.
 *
 * <p>ASIS: Spring MVC 4.x (javax.servlet 기반)<br>
 * TOBE: Spring MVC 6.x (jakarta.servlet 기반, Spring Boot 3)</p>
 */
@RestController
public class HealthCheckController {

    /**
     * 헬스체크 엔드포인트.
     *
     * @return "OK" 문자열
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    /**
     * 상태 정보 엔드포인트.
     *
     * @return 애플리케이션 정보 맵
     */
    @GetMapping("/")
    public Map<String, String> index() {
        return Map.of(
                "app", "전자투표 시스템 - JDK 17 + Boot 3 라이브러리 호환성 검증",
                "stack", "Spring Boot 3.5.11 + MyBatis 3.0.3 + JDK 17",
                "time", LocalDateTime.now().toString()
        );
    }
}
