package com.ss.evote.commons;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Apache Commons Lang3 테스트.
 *
 * <p>ASIS: commons-lang 2.x (commons-lang:commons-lang, org.apache.commons.lang)<br>
 * TOBE: commons-lang3 (Boot 관리 버전, org.apache.commons.lang3)</p>
 *
 * <p>전환 이유: lang 2.x → lang3로 패키지 변경. StringUtils, ObjectUtils 등 API 개선.</p>
 */
class Lang3Test {

    @Test
    @DisplayName("[TOBE] commons-lang3 Boot 관리 버전 - StringUtils.isBlank 동작 확인")
    void stringUtilsIsBlank() {
        assertTrue(StringUtils.isBlank(null), "null은 blank여야 한다");
        assertTrue(StringUtils.isBlank(""), "빈 문자열은 blank여야 한다");
        assertTrue(StringUtils.isBlank("   "), "공백만 있는 문자열은 blank여야 한다");
        assertFalse(StringUtils.isBlank("evote"), "일반 문자열은 blank가 아니어야 한다");
    }

    @Test
    @DisplayName("[TOBE] commons-lang3 Boot 관리 버전 - StringUtils.abbreviate 동작 확인")
    void stringUtilsAbbreviate() {
        String longText = "이것은 전자투표 시스템의 매우 긴 설명 문자열입니다. 일정 길이 이상은 잘라냅니다.";

        String abbreviated = StringUtils.abbreviate(longText, 10);

        assertTrue(abbreviated.length() <= 10, "축약된 문자열은 최대 길이 이하여야 한다");
        assertTrue(abbreviated.endsWith("..."), "축약된 문자열은 '...'으로 끝나야 한다");
    }
}
