package com.ss.evote.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Xerces XML 파싱 테스트.
 *
 * <p>ASIS: xercesImpl 2.x<br>
 * TOBE: xercesImpl 2.12.2</p>
 *
 * <p>전환 이유: XML 파싱 보안 패치 적용, XXE 방어 기본값 강화.</p>
 */
class XercesTest {

    private static final String SAMPLE_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<election>"
            + "  <title>전자투표 시스템</title>"
            + "  <year>2026</year>"
            + "</election>";

    @Test
    @DisplayName("[TOBE] xercesImpl 2.12.2 - DocumentBuilderFactory 생성 확인")
    void createDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        assertNotNull(factory, "DocumentBuilderFactory가 null이면 안 된다");
    }

    @Test
    @DisplayName("[TOBE] xercesImpl 2.12.2 - XML 파싱 확인")
    void parseXml() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input = new ByteArrayInputStream(
                SAMPLE_XML.getBytes(StandardCharsets.UTF_8));
        Document document = builder.parse(input);
        assertNotNull(document, "파싱된 Document가 null이면 안 된다");

        Element root = document.getDocumentElement();
        assertEquals("election", root.getTagName(), "루트 엘리먼트 이름은 'election'이어야 한다");

        String title = root.getElementsByTagName("title").item(0).getTextContent();
        assertEquals("전자투표 시스템", title, "title 엘리먼트의 텍스트가 일치해야 한다");

        String year = root.getElementsByTagName("year").item(0).getTextContent();
        assertEquals("2026", year, "year 엘리먼트의 텍스트가 일치해야 한다");
    }
}
