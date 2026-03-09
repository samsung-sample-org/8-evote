package com.ss.evote.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 설정.
 *
 * <p>ASIS: mybatis 3.2.x (수동 SqlSessionFactory 설정, XML 기반 빈 정의)<br>
 * TOBE: mybatis-spring-boot-starter 3.0.3 (Spring Boot 자동 구성)</p>
 *
 * <p>전환 이유:</p>
 * <ul>
 *   <li>mybatis-spring-boot-starter는 SqlSessionFactory, SqlSessionTemplate을 자동 구성한다.</li>
 *   <li>@MapperScan으로 Mapper 인터페이스를 자동 등록한다.</li>
 *   <li>application.yml의 mybatis.* 설정으로 XML Mapper 위치 및 옵션을 지정한다.</li>
 * </ul>
 *
 * <p>주의: 본 프로젝트는 JPA(Hibernate) 대신 MyBatis를 사용한다.
 * spring-boot-starter-data-jpa는 의존성에서 제외되었다.</p>
 */
@Configuration
@MapperScan("com.ss.evote.mapper")
public class MyBatisConfig {
    // mybatis-spring-boot-starter가 SqlSessionFactory, SqlSessionTemplate을 자동 구성한다.
    // application.yml의 mybatis.mapper-locations 설정으로 XML Mapper 위치를 지정한다.
}
