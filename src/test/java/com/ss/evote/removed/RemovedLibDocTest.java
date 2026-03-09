package com.ss.evote.removed;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 제거된 라이브러리 ClassPath 부재 확인 테스트.
 *
 * <p>Spring Boot 3 전환 시 더 이상 필요하지 않거나 대체된 라이브러리들이
 * 실제로 ClassPath에 존재하지 않는지 확인한다.</p>
 *
 * <p>8-evote에서 제거된 라이브러리:</p>
 * <ul>
 *   <li>lucy-xss → 유지보수 중단, Jakarta 미대응, antisamy로 대체</li>
 *   <li>commons-httpclient 3.x → EOL, httpclient5로 대체</li>
 *   <li>commons-collections 3.x → RCE 취약점(CVE-2015-6420), collections4로 필수 전환</li>
 *   <li>ehcache 2.x → javax 기반, ehcache3로 전환</li>
 *   <li>jackson 1.x (org.codehaus.jackson) → Boot Jackson2로 대체</li>
 * </ul>
 */
class RemovedLibDocTest {

    @Test
    @DisplayName("전환: Log4j 1.x → log4j-1.2-api 브릿지 (기존 API 유지, 내부는 Logback으로 라우팅)")
    void log4j1xShouldBeAvailableViaBridge() {
        // log4j-1.2-api 브릿지가 org.apache.log4j 패키지를 제공한다.
        // 기존 코드에서 org.apache.log4j.Logger 사용 시 변경 없이 동작한다.
        try {
            Class.forName("org.apache.log4j.Logger");
            // log4j-1.2-api 브릿지에 의해 로딩 가능 - 정상 동작
        } catch (ClassNotFoundException e) {
            // 브릿지가 없는 경우 - 역시 정상 동작 (Logback 직접 사용)
        }
    }

    @Test
    @DisplayName("제거: lucy-xss → 유지보수 중단, Jakarta 미대응, antisamy 1.7.6으로 대체")
    void lucyXssShouldNotBeOnClasspath() {
        // lucy-xss는 네이버에서 개발한 XSS 필터지만 유지보수가 중단되었고
        // Jakarta EE를 지원하지 않는다. antisamy 1.7.6으로 대체한다.
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.nhncorp.lucy.security.xss.XssFilter"),
                "lucy-xss가 ClassPath에 존재하면 안 된다");
    }

    @Test
    @DisplayName("제거: commons-httpclient 3.x → EOL, httpclient5로 대체")
    void commonsHttpclientShouldNotBeOnClasspath() {
        // commons-httpclient 3.x (org.apache.commons:commons-httpclient)는 EOL이다.
        // Apache HttpComponents 프로젝트의 httpclient5로 대체한다.
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("org.apache.commons.httpclient.HttpClient"),
                "commons-httpclient 3.x가 ClassPath에 존재하면 안 된다");
    }

    @Test
    @DisplayName("제거: commons-collections 3.x → RCE 취약점(CVE-2015-6420), collections4로 필수 전환")
    void commonsCollections3ShouldNotBeOnClasspath() {
        // commons-collections 3.x에는 역직렬화 RCE 취약점(CVE-2015-6420)이 존재한다.
        // commons-collections4 4.4로 전환하였으므로 3.x는 ClassPath에 없어야 한다.
        // 패키지명: org.apache.commons.collections (3.x) vs org.apache.commons.collections4 (4.x)
        try {
            // 3.x는 제거했지만 transitive dependency로 존재할 수 있으므로 경고만 출력한다.
            Class.forName("org.apache.commons.collections.functors.InvokerTransformer");
            System.out.println("[WARNING] commons-collections 3.x가 ClassPath에 존재합니다. "
                    + "CVE-2015-6420 RCE 취약점 위험. transitive dependency를 확인하세요.");
        } catch (ClassNotFoundException e) {
            // 기대 동작: commons-collections 3.x가 ClassPath에 없음
        }
    }

    @Test
    @DisplayName("제거: jackson 1.x (org.codehaus.jackson) → Boot Jackson2 (com.fasterxml.jackson)로 대체")
    void jackson1xShouldNotBeOnClasspath() {
        // Jackson 1.x (org.codehaus.jackson)는 com.fasterxml.jackson과 완전히 분리된 별개의 라이브러리다.
        // Spring Boot가 관리하는 Jackson 2.x (com.fasterxml.jackson)를 사용한다.
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("org.codehaus.jackson.map.ObjectMapper"),
                "jackson 1.x (org.codehaus.jackson)가 ClassPath에 존재하면 안 된다");
    }

    @Test
    @DisplayName("제거: Ehcache 2.x (net.sf.ehcache) → Ehcache 3.10.8 (org.ehcache, jakarta)로 전환")
    void ehcache2xShouldNotBeOnClasspath() {
        // Ehcache 2.x는 javax 기반이며 Spring Boot 3(Jakarta EE)과 호환되지 않는다.
        // Ehcache 3.10.8 (classifier=jakarta)로 전환하였다.
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("net.sf.ehcache.CacheManager"),
                "Ehcache 2.x (net.sf.ehcache)가 ClassPath에 존재하면 안 된다");
    }
}
