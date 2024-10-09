package com.reboot_course.review_service.domain.review.exception;

public class DuplicateReviewException extends ReviewException {
    public DuplicateReviewException(Long userId, Long productId) {
        super(String.format("사용자(id : %d)는 이미 상품(id : %d)에 대한 리뷰를 작성했습니다.", userId, productId));
    }
}