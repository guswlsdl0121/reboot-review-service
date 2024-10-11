package com.reboot_course.review_service.domain.product.service;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.exception.ProductNotFoundException;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductFinder {
    private final ProductRepository productRepository;

    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 10, multiplier = 2, maxDelay = 100)
    )
    public Product fetchOneAndUpdateStat(Long productId, int score) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.updateStat(score);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product fetchOne(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
