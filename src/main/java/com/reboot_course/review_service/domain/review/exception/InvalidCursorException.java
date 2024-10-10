package com.reboot_course.review_service.domain.review.exception;

import com.reboot_course.review_service.common.exception.base.NotFoundException;

public class InvalidCursorException extends NotFoundException {
    public InvalidCursorException(Long cursorId) {
        super(String.format("커서 값(%d) 에 해당하는 product가 존재하지 않습니다!", cursorId));
    }
}
