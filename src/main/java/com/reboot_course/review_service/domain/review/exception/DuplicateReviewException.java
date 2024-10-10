package com.reboot_course.review_service.domain.review.exception;

import com.reboot_course.review_service.common.exception.base.ConflictException;

public class DuplicateReviewException extends ConflictException {
    public DuplicateReviewException(Long userId, Long productId) {
        super(String.format("사용자(id : %d)가 이미 상품(id : %d)에 대한 리뷰를 작성했습니다.", userId, productId));
    }
}