package com.reboot_course.review_service.domain.product.service;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.exception.ProductNotFoundException;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFinder {
    private final ProductRepository productRepository;

    public Product fetch(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
