package com.reboot_course.review_service.controller;

import com.reboot_course.review_service.dto.common.CommonResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReviewExceptionHandler {
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<CommonResponse> NotFound(EntityNotFoundException e) {
        CommonResponse res = new CommonResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
}
