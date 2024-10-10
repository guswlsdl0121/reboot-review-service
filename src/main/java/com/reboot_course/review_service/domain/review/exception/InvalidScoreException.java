package com.reboot_course.review_service.domain.review.exception;

import com.reboot_course.review_service.common.constant.GlobalConstants;
import com.reboot_course.review_service.common.exception.base.BadRequestException;

public class InvalidScoreException extends BadRequestException {
    public InvalidScoreException() {
        super(String.format("리뷰 점수는 %d에서 %d 사이의 값이어야 합니다.", GlobalConstants.MIN_SCORE, GlobalConstants.MAX_SCORE));
    }
}
