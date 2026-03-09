package com.ss.evote.boot;

import com.ss.evote.mapper.SampleMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MyBatis 매퍼 슬라이스 테스트.
 *
 * <p>ASIS: mybatis 3.2.x (수동 SqlSessionFactory 설정, 수동 매퍼 등록)<br>
 * TOBE: mybatis-spring-boot-starter 3.0.3 (@MybatisTest 슬라이스 테스트)</p>
 *
 * <p>전환 이유:</p>
 * <ul>
 *   <li>mybatis-spring-boot-starter는 @MybatisTest 슬라이스 테스트를 지원한다.</li>
 *   <li>H2 인메모리 DB로 자동 구성되어 별도 DB 설정 없이 Mapper 단위 테스트가 가능하다.</li>
 *   <li>@SpringBootTest보다 가벼운 컨텍스트로 Mapper 계층만 테스트할 수 있다.</li>
 * </ul>
 *
 * <p><b>이 테스트는 8-evote의 핵심 검증 포인트이다.</b>
 * JPA(Hibernate) 대신 MyBatis 3.0.3 매퍼가 정상 동작하는지 확인한다.</p>
 */
@MybatisTest
class MyBatisTest {

    @Autowired
    private SampleMapper sampleMapper;

    @Test
    @DisplayName("[TOBE] MyBatis 3.0.3 - SampleMapper 주입 확인")
    void mapperInjected() {
        // @MybatisTest로 자동 구성된 SampleMapper가 주입되는지 확인한다
        assertNotNull(sampleMapper, "SampleMapper가 null이면 안 된다");
    }

    @Test
    @DisplayName("[TOBE] MyBatis 3.0.3 - @Select 어노테이션 기반 SELECT 1 동작 확인")
    void testSelectOneAnnotation() {
        // @Select("SELECT 1") 어노테이션 방식 Mapper 동작 확인
        int result = sampleMapper.selectOne();
        assertEquals(1, result, "SELECT 1의 결과는 1이어야 한다");
    }

    @Test
    @DisplayName("[TOBE] MyBatis 3.0.3 - XML Mapper 기반 SELECT 1 동작 확인")
    void testSelectOneFromXml() {
        // SampleMapper.xml에 정의된 XML Mapper 방식 동작 확인
        int result = sampleMapper.selectOneFromXml();
        assertEquals(1, result, "XML Mapper의 SELECT 1 결과는 1이어야 한다");
    }
}
