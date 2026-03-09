package com.ss.evote.config;

import com.ss.evote.job.SampleQuartzJob;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Quartz 스케줄러 설정.
 *
 * <p>ASIS: Quartz 1.x (독립 설정, quartz.properties 직접 관리)<br>
 * TOBE: spring-boot-starter-quartz (Quartz 2.5+, Spring Boot 자동 구성)</p>
 *
 * <p>전환 이유:</p>
 * <ul>
 *   <li>Quartz 1.x는 Spring Boot 3과 호환되지 않으며, API가 크게 변경되었다.</li>
 *   <li>Spring Boot의 spring-boot-starter-quartz는 Quartz 2.5+를 포함하고,
 *       자동 구성(Auto Configuration)을 통해 {@code SchedulerFactoryBean}을 자동 등록한다.</li>
 *   <li>JobDetail과 Trigger를 Spring Bean으로 등록하면 자동으로 스케줄러에 등록된다.</li>
 * </ul>
 */
@Configuration
public class QuartzConfig {

    /**
     * 샘플 Quartz Job의 상세 정보를 정의한다.
     *
     * @return JobDetail 팩토리 빈
     */
    @Bean
    public JobDetailFactoryBean sampleJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(SampleQuartzJob.class);
        factory.setName("sampleQuartzJob");
        factory.setGroup("evote-group");
        factory.setDescription("샘플 Quartz Job - 전자투표 시스템 라이브러리 호환성 검증용");
        factory.setDurability(true);
        return factory;
    }

    /**
     * 샘플 Job의 트리거를 정의한다 (10초 간격 반복 실행).
     *
     * @param sampleJobDetail 연결할 JobDetail
     * @return SimpleTrigger 팩토리 빈
     */
    @Bean
    public SimpleTriggerFactoryBean sampleJobTrigger(JobDetail sampleJobDetail) {
        SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
        factory.setJobDetail(sampleJobDetail);
        factory.setName("sampleJobTrigger");
        factory.setGroup("evote-group");
        factory.setRepeatInterval(10_000L);
        factory.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return factory;
    }
}
