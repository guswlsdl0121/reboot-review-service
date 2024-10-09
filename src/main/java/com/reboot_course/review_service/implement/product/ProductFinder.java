package com.reboot_course.review_service.implement.product;

import com.reboot_course.review_service.entity.Product;
import com.reboot_course.review_service.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFinder {
    private final ProductRepository productRepository;

    public Product fetch(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("상품(id : %d)을 찾을 수 없습니다.", productId)));
    }
}
