package com.ss.evote.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * MyBatis 샘플 Mapper 인터페이스.
 *
 * <p>ASIS: mybatis 3.2.x (XML Mapper 수동 등록, SqlSessionTemplate 수동 주입)<br>
 * TOBE: mybatis-spring-boot-starter 3.0.3 (@Mapper + @MapperScan 자동 등록)</p>
 *
 * <p>전환 이유: Spring Boot Starter가 SqlSessionFactory와 SqlSessionTemplate을 자동 구성하고,
 * @MapperScan으로 Mapper 인터페이스를 Bean으로 자동 등록한다.</p>
 */
@Mapper
public interface SampleMapper {

    /**
     * DB 연결 확인용 SELECT 1 쿼리.
     *
     * @return 1
     */
    @Select("SELECT 1")
    int selectOne();

    /**
     * XML Mapper를 통한 SELECT 1 쿼리 (SampleMapper.xml에 정의).
     *
     * @return 1
     */
    int selectOneFromXml();
}
