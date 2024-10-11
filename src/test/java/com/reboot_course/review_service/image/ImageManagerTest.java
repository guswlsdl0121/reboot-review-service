package com.reboot_course.review_service.image;

import com.reboot_course.review_service.infrastructure.image.ImageManager;
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

    @TempDir
    Path tempDir;
    private ImageManager imageManager;

    @BeforeEach
    void setUp() {
        imageManager = new ImageManager();
        ReflectionTestUtils.setField(imageManager, "uploadDir", tempDir.toString());
    }

    @Test
    @DisplayName("이미지 업로드 시 올바른 경로와 확장자를 가진 파일명이 반환되어야 한다")
    void t1() {
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
    @DisplayName("null 파일 업로드 시 null이 반환되어야 한다")
    void t2() {
        // When
        String result = imageManager.upload(null);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("빈 이미지 파일 업로드 시 null이 반환되어야 한다")
    void t3() {
        // Given
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);

        // When
        String result = imageManager.upload(file);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("업로드된 이미지 파일을 이름으로 조회 시 해당 파일의 Resource가 반환되어야 한다")
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
    @DisplayName("존재하지 않는 이미지 파일명으로 조회 시 RuntimeException이 발생해야 한다")
    void t5() {
        // Given
        String nonExistingFilename = "non_existing.jpg";

        // When & Then
        assertThrows(RuntimeException.class, () -> imageManager.findOneByName(nonExistingFilename));
    }
}