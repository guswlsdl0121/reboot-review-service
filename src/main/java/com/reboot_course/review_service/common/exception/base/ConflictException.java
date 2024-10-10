package com.reboot_course.review_service.common.exception.base;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}