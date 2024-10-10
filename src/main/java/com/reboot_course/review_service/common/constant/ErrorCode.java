package com.reboot_course.review_service.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
    CONFLICT(HttpStatus.CONFLICT, "Conflicting request"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request");

    private final HttpStatus status;
    private final String message;
}