package com.reboot_course.review_service.dto.request;

import lombok.Builder;

@Builder
public record ReviewCreateRequest(
        Long userId,
        Integer score,
        String content
) {
}