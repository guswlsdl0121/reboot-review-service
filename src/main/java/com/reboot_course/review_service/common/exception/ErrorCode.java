package com.reboot_course.review_service.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Entity not found"),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "Duplicate review"),
    INVALID_SCORE(HttpStatus.BAD_REQUEST, "Invalid score");

    private final HttpStatus status;
    private final String message;
}