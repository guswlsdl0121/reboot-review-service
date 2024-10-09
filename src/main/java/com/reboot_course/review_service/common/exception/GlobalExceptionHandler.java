package com.reboot_course.review_service.common.exception;

import com.reboot_course.review_service.common.dto.ErrorResponse;
import com.reboot_course.review_service.domain.review.exception.DuplicateReviewException;
import com.reboot_course.review_service.domain.review.exception.InvalidScoreException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException e, HttpServletRequest request) {
        ErrorResponse res = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(res.errorCode().getStatus()).body(res);
    }

    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReview(DuplicateReviewException e, HttpServletRequest request) {
        ErrorResponse res = ErrorResponse.of(ErrorCode.DUPLICATE_REVIEW, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(res.errorCode().getStatus()).body(res);
    }

    @ExceptionHandler(InvalidScoreException.class)
    public ResponseEntity<ErrorResponse> handleInvalidScore(InvalidScoreException e, HttpServletRequest request) {
        ErrorResponse res = ErrorResponse.of(ErrorCode.INVALID_SCORE, request.getRequestURI(), e.getMessage());
        return ResponseEntity.status(res.errorCode().getStatus()).body(res);
    }
}