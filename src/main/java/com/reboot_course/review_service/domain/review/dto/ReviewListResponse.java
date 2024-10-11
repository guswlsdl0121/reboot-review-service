package com.reboot_course.review_service.domain.review.dto;

import java.util.List;

public record ReviewListResponse(
        long totalCount,
        float score,
        Long cursor,
        List<ReviewResponse> reviews
) {
}