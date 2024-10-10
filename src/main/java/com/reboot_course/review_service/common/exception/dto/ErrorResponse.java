package com.reboot_course.review_service.common.exception.dto;

import com.reboot_course.review_service.common.constant.ErrorCode;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        ErrorCode errorCode,
        String path,
        LocalDateTime timestamp
) {

    public static ErrorResponse of(ErrorCode errorCode, String path, String customMessage) {
        return new ErrorResponse(customMessage, errorCode, path, LocalDateTime.now());
    }
}