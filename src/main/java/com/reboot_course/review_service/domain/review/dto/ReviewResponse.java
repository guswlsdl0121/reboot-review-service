package com.reboot_course.review_service.domain.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long userId,
        int score,
        String content,
        String imageUrl,
        LocalDateTime createdAt
) {}