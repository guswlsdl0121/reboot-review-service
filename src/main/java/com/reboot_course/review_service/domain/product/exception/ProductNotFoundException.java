package com.reboot_course.review_service.domain.product.exception;

import com.reboot_course.review_service.common.exception.base.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(Long productId) {
        super(String.format("상품(id : %d)을 찾을 수 없습니다.", productId));
    }
}