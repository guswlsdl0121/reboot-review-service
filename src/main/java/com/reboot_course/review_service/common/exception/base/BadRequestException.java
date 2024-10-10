package com.reboot_course.review_service.common.exception.base;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}