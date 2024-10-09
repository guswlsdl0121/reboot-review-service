package com.reboot_course.review_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


class ImageManagerTest {

    private ImageManager imageManager;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        imageManager = new ImageManager();
        ReflectionTestUtils.setField(imageManager, "uploadDir", tempDir.toString());
    }

    @Test
    @DisplayName("성공 테스트: 이미지 저장")
    void t1() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test image content".getBytes());

        // When
        String result = imageManager.upload(file);

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("/images/"));
        assertTrue(result.endsWith(".jpg"));
    }

    @Test
    @DisplayName("성공 테스트: null 전달 시 null 저장")
    void t2() throws IOException {
        // When
        String result = imageManager.upload(null);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("성공 테스트: 빈 이미지 전달 시 빈 이미지 저장")
    void t3() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);

        // When
        String result = imageManager.upload(file);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("성공 테스트: 이미지 저장 후 파일 이름으로 조회")
    void t4() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test image content".getBytes());
        String uploadedFilePath = imageManager.upload(file);
        String filename = uploadedFilePath.substring(uploadedFilePath.lastIndexOf("/") + 1);

        // When
        Resource resource = imageManager.findOneByName(filename);

        // Then
        assertTrue(resource.exists());
        try (InputStream is = resource.getInputStream()) {
            assertEquals("test image content", new String(is.readAllBytes()));
        }
    }

    @Test
    @DisplayName("실패 테스트: 존재하지 않는 이름으로 찾으려 할 때 오류 발생")
    void t5() {
        // Given
        String nonExistingFilename = "non_existing.jpg";

        // When & Then
        assertThrows(RuntimeException.class, () -> imageManager.findOneByName(nonExistingFilename));
    }
}