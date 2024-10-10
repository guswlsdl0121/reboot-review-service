package com.reboot_course.review_service.common.exception.handler;

import com.reboot_course.review_service.common.constant.ErrorCode;
import com.reboot_course.review_service.common.exception.base.BadRequestException;
import com.reboot_course.review_service.common.exception.base.ConflictException;
import com.reboot_course.review_service.common.exception.base.NotFoundException;
import com.reboot_course.review_service.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e, HttpServletRequest request) {
        ErrorResponse res = ErrorResponse.of(ErrorCode.NOT_FOUND, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(res.errorCode().getStatus()).body(res);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException e, HttpServletRequest request) {
        ErrorResponse res = ErrorResponse.of(ErrorCode.CONFLICT, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(res.errorCode().getStatus()).body(res);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e, HttpServletRequest request) {
        ErrorResponse res = ErrorResponse.of(ErrorCode.BAD_REQUEST, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(res.errorCode().getStatus()).body(res);
    }
}
