package com.reboot_course.review_service.product;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.product.service.ProductFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductFinderTest {

    @Autowired
    private ProductFinder productFinder;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("동시에 여러 스레드가 상품을 업데이트할 때 모든 업데이트가 성공해야 한다")
    void t1() throws InterruptedException {
        Product testProduct = productRepository.save(Product.builder()
                .reviewCount(0L)
                .averageScore(0F)
                .build());

        int numberOfThreads = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    Product updatedProduct = productFinder.fetchOneAndUpdateStat(testProduct.getId(), 5);
                    System.out.println("Updated product: " + updatedProduct);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("Unexpected exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        System.out.println("Successful updates: " + successCount.get());

        assertEquals(numberOfThreads, successCount.get(), "All updates should succeed");

        Product finalProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        System.out.println("Final product state: " + finalProduct);
        assertEquals(numberOfThreads, finalProduct.getReviewCount(), "Review count should match total updates");
        assertEquals(5.0f, finalProduct.getAverageScore(), "Average score should be 5.0");
    }
}