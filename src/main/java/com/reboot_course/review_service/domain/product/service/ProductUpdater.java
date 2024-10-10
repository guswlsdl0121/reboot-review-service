package com.reboot_course.review_service.domain.product.service;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.exception.ProductNotFoundException;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductUpdater {
    private final ProductRepository productRepository;

    @Transactional
    public Product getOneAndUpdateStat(Long productId, int score) {
        // 비관적 락을 사용하여 Product를 조회
        Product product = productRepository.findByIdWithPessimisticLock(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Product의 상태를 업데이트
        product.updateStat(score);

        // 업데이트된 Product를 저장
        return productRepository.save(product);
    }
}
