package com.ss.evote.commons;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Apache Commons BeanUtils 테스트.
 *
 * <p>ASIS: commons-beanutils 1.8.x<br>
 * TOBE: commons-beanutils 1.9.4</p>
 *
 * <p>전환 이유: CVE-2014-0114 등 보안 취약점 패치, PropertyUtils 개선.</p>
 */
class BeanutilsTest {

    public static class VoteSource {
        private String candidateName;
        private int voteCount;

        public VoteSource() {}
        public VoteSource(String candidateName, int voteCount) {
            this.candidateName = candidateName;
            this.voteCount = voteCount;
        }
        public String getCandidateName() { return candidateName; }
        public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
        public int getVoteCount() { return voteCount; }
        public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    }

    public static class VoteTarget {
        private String candidateName;
        private int voteCount;

        public String getCandidateName() { return candidateName; }
        public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
        public int getVoteCount() { return voteCount; }
        public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    }

    @Test
    @DisplayName("[TOBE] commons-beanutils 1.9.4 - copyProperties 동작 확인")
    void copyProperties() throws Exception {
        VoteSource source = new VoteSource("홍길동", 150);
        VoteTarget target = new VoteTarget();

        BeanUtils.copyProperties(target, source);

        assertEquals("홍길동", target.getCandidateName(), "후보자 이름이 복사되어야 한다");
        assertEquals(150, target.getVoteCount(), "득표수가 복사되어야 한다");
    }
}
