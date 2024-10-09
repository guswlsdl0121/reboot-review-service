package com.reboot_course.review_service.domain.review.exception;

public class InvalidScoreException extends ReviewException {
    public InvalidScoreException() {
        super("리뷰 점수는 1에서 5 사이의 값이어야 합니다.");
    }
}

