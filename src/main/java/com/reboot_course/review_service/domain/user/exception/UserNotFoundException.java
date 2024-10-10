package com.reboot_course.review_service.domain.user.exception;

import com.reboot_course.review_service.common.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long userId) {
        super(String.format("사용자(id : %d)을 찾을 수 없습니다.", userId));
    }
}