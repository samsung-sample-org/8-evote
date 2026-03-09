package com.ss.evote.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JSON 라이브러리 테스트.
 *
 * <p>ASIS: jackson 1.x (org.codehaus.jackson) + json-simple 구버전<br>
 * TOBE: Gson 2.10.1 (신규 권장) + json-simple 1.1.1 (기존 코드 유지)</p>
 *
 * <p>jackson 1.x는 Boot의 Jackson 2.x(com.fasterxml.jackson)와 완전 분리되어
 * 별도로 유지할 필요가 없다. 신규 코드에는 Gson 사용을 권장한다.</p>
 */
class JsonTest {

    @Test
    @DisplayName("[TOBE] Gson 2.10.1 - JsonObject 생성 및 직렬화 확인")
    void gsonJsonObject() {
        Gson gson = new Gson();

        JsonObject obj = new JsonObject();
        obj.addProperty("candidate", "홍길동");
        obj.addProperty("votes", 150);

        String json = gson.toJson(obj);
        assertNotNull(json, "JSON 문자열이 null이면 안 된다");

        JsonObject parsed = gson.fromJson(json, JsonObject.class);
        assertEquals("홍길동", parsed.get("candidate").getAsString(),
                "후보자 이름이 일치해야 한다");
        assertEquals(150, parsed.get("votes").getAsInt(),
                "득표수가 일치해야 한다");
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("[기존 유지] json-simple 1.1.1 - JSONObject 동작 확인 (기존 코드 유지)")
    void jsonSimple() {
        // json-simple은 비권장이지만 기존 코드 호환을 위해 유지한다.
        // 신규 코드에는 Gson 사용을 권장한다.
        JSONObject obj = new JSONObject();
        obj.put("status", "success");
        obj.put("count", 3);

        assertNotNull(obj, "JSONObject가 null이면 안 된다");
        assertEquals("success", obj.get("status"), "status 값이 일치해야 한다");
        assertEquals(3, obj.get("count"), "count 값이 일치해야 한다");
    }
}
