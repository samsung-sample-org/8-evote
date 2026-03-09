package com.ss.evote.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * jsoup HTML 파싱 테스트.
 *
 * <p>ASIS: nekohtml 1.x (HTML 파싱)<br>
 * TOBE: jsoup 1.18.1</p>
 *
 * <p>전환 이유: NekoHTML EOL, Jsoup이 사실상 표준 HTML 파서로 자리잡음.
 * XSS 방어 기능도 내장하고 있어 선호도가 높다.</p>
 */
class JsoupTest {

    @Test
    @DisplayName("[TOBE] jsoup 1.18.1 - HTML 파싱 확인")
    void parseHtml() {
        String html = "<html><body><h1>전자투표</h1><p id='info'>투표를 진행해주세요.</p></body></html>";

        Document doc = Jsoup.parse(html);
        assertNotNull(doc, "파싱된 Document가 null이면 안 된다");

        Element h1 = doc.select("h1").first();
        assertNotNull(h1, "h1 엘리먼트가 null이면 안 된다");
        assertEquals("전자투표", h1.text(), "h1 텍스트가 일치해야 한다");

        Element p = doc.getElementById("info");
        assertNotNull(p, "id='info' 엘리먼트가 null이면 안 된다");
        assertEquals("투표를 진행해주세요.", p.text(), "p 텍스트가 일치해야 한다");
    }
}
