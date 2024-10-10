package com.reboot_course.review_service.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ReviewCreateRequest(
        @JsonProperty("userId")
        Long memberId,
        Integer score,
        String content
) {
}