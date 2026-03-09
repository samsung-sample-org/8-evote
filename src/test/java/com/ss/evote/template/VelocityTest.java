package com.ss.evote.template;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Apache Velocity 템플릿 엔진 테스트.
 *
 * <p>ASIS: velocity 1.x (org.apache.velocity:velocity)<br>
 * TOBE: velocity-engine-core 2.3 (org.apache.velocity:velocity-engine-core)</p>
 *
 * <p>전환 이유: Velocity 1.x는 EOL이며, 2.x에서 artifactId가 변경되었다.
 * Spring Boot 3에는 Velocity auto-config이 없으므로 수동으로 VelocityEngine을
 * 초기화하여 사용해야 한다.</p>
 */
class VelocityTest {

    @Test
    @DisplayName("[TOBE] velocity-engine-core 2.3 - VelocityEngine 초기화 확인")
    void initVelocityEngine() {
        VelocityEngine engine = new VelocityEngine();
        engine.init();
        assertNotNull(engine, "VelocityEngine이 null이면 안 된다");
    }

    @Test
    @DisplayName("[TOBE] velocity-engine-core 2.3 - 템플릿 렌더링 확인")
    void renderTemplate() {
        VelocityEngine engine = new VelocityEngine();
        engine.init();

        String template = "전자투표 결과 - 후보: ${candidate}, 득표수: ${votes}표";

        VelocityContext context = new VelocityContext();
        context.put("candidate", "홍길동");
        context.put("votes", 150);

        StringWriter writer = new StringWriter();
        engine.evaluate(context, writer, "evote-template", template);

        String result = writer.toString();
        assertNotNull(result, "렌더링 결과가 null이면 안 된다");
        assertTrue(result.contains("홍길동"), "렌더링 결과에 후보자 이름이 포함되어야 한다");
        assertTrue(result.contains("150"), "렌더링 결과에 득표수가 포함되어야 한다");
    }
}
