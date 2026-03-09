package com.ss.evote.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Ehcache 3 JCache 통합 테스트.
 *
 * <p>ASIS: ehcache 2.x (javax 기반, net.sf.ehcache)<br>
 * TOBE: ehcache 3.10.8 (jakarta 호환, JCache/JSR-107 표준, org.ehcache)</p>
 *
 * <p>전환 이유: Ehcache 2.x는 EOL이며 Spring Boot 3(Jakarta EE)과 호환되지 않는다.
 * Ehcache 3.x는 JCache(JSR-107) 표준을 완전 구현하며, Spring Boot의
 * {@code spring.cache.jcache.config} 설정을 통해 자동 연동된다.</p>
 */
@SpringBootTest
class Ehcache3Test {

    @Autowired
    private CacheManager cacheManager;

    @Test
    @DisplayName("[TOBE] ehcache 3.10.8 - CacheManager 주입 확인")
    void cacheManagerInjected() {
        assertNotNull(cacheManager, "CacheManager가 null이면 안 된다");
    }

    @Test
    @DisplayName("[TOBE] ehcache 3.10.8 - sampleCache 존재 확인")
    void sampleCacheExists() {
        Cache sampleCache = cacheManager.getCache("sampleCache");
        assertNotNull(sampleCache, "sampleCache가 존재해야 한다");
    }

    @Test
    @DisplayName("[TOBE] ehcache 3.10.8 - 캐시 put/get 동작 확인")
    void cachePutAndGet() {
        Cache sampleCache = cacheManager.getCache("sampleCache");
        assertNotNull(sampleCache, "sampleCache가 존재해야 한다");

        String key = "voteKey";
        String value = "전자투표 캐시 테스트 값";

        sampleCache.put(key, value);

        Cache.ValueWrapper wrapper = sampleCache.get(key);
        assertNotNull(wrapper, "캐시에서 조회한 ValueWrapper가 null이면 안 된다");
        assertEquals(value, wrapper.get(), "캐시에서 조회한 값이 저장한 값과 일치해야 한다");
    }
}
