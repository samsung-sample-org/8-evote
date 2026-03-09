package com.ss.evote.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * dom4j XML 처리 테스트.
 *
 * <p>ASIS: dom4j 1.x (dom4j:dom4j)<br>
 * TOBE: dom4j 2.1.4 (org.dom4j:dom4j)</p>
 *
 * <p>전환 이유: dom4j 1.x에 XXE 취약점 존재, 2.x에서 groupId가 변경되고 보안 패치가 적용되었다.</p>
 */
class Dom4jTest {

    @Test
    @DisplayName("[TOBE] dom4j 2.1.4 - Document 생성 확인")
    void createDocument() {
        Document document = DocumentHelper.createDocument();
        assertNotNull(document, "Document 객체가 null이면 안 된다");
    }

    @Test
    @DisplayName("[TOBE] dom4j 2.1.4 - 전자투표 XML 구조 생성 확인")
    void addElement() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("election");
        root.addAttribute("type", "electronic");

        Element candidate = root.addElement("candidate");
        candidate.setText("홍길동");

        assertEquals("election", document.getRootElement().getName(),
                "루트 엘리먼트 이름은 'election'이어야 한다");
        assertEquals("electronic", root.attributeValue("type"),
                "루트 엘리먼트의 type 속성이 일치해야 한다");
        assertEquals("홍길동", candidate.getText(),
                "후보자 엘리먼트의 텍스트가 일치해야 한다");
    }

    @Test
    @DisplayName("[TOBE] dom4j 2.1.4 - XML 문자열 출력 확인")
    void documentToXmlString() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("votes");
        root.addElement("vote").setText("찬성");

        String xml = document.asXML();
        assertNotNull(xml, "XML 문자열이 null이면 안 된다");
        assertTrue(xml.contains("<votes>"), "XML에 <votes> 태그가 포함되어야 한다");
        assertTrue(xml.contains("<vote>찬성</vote>"), "XML에 <vote>찬성</vote> 내용이 포함되어야 한다");
    }
}
