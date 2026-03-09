package com.ss.evote.commons;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Apache Commons Compress 테스트.
 *
 * <p>ASIS: commons-compress 1.x<br>
 * TOBE: commons-compress 1.26.1</p>
 *
 * <p>전환 이유: 압축/압축해제 보안 패치, JDK 17 호환, Zip Bomb 방어 강화.</p>
 */
class CompressTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("[TOBE] commons-compress 1.26.1 - ZIP 아카이브 생성 확인")
    void createZipArchive() throws Exception {
        File zipFile = tempDir.resolve("evote-result.zip").toFile();

        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new FileOutputStream(zipFile))) {
            ZipArchiveEntry entry = new ZipArchiveEntry("vote-result.txt");
            byte[] content = "전자투표 결과: 홍길동 150표".getBytes(StandardCharsets.UTF_8);
            entry.setSize(content.length);
            zos.putArchiveEntry(entry);
            zos.write(content);
            zos.closeArchiveEntry();
        }

        assertTrue(zipFile.exists(), "ZIP 파일이 생성되어야 한다");
        assertTrue(zipFile.length() > 0, "ZIP 파일 크기가 0보다 커야 한다");
    }
}
