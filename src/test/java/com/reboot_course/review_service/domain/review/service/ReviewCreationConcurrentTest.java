package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ReviewCreationConcurrentTest {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Product testProduct;
    private List<Member> testMembers;

    @BeforeEach
    public void setup() {
        testProduct = productRepository.save(Product.builder()
                .reviewCount(0L)
                .averageScore(0.0f)
                .build());

        testMembers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testMembers.add(memberRepository.save(new Member()));
        }
    }

    @Test
    @DisplayName("동시성 테스트 : 10명의 사용자가 한 개의 product에 대해 동시에 리뷰를 작성하고자 할 때")
    public void t1() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    ReviewCreateRequest request = new ReviewCreateRequest(
                            testMembers.get(finalI).getId(),
                            5,
                            "Great product!");
                    reviewService.create(testProduct.getId(), request, null);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 완료될 때까지 대기
        latch.await();

        // 데이터베이스에서 최신 상태의 Product를 가져옴
        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();

        // 검증
        assertEquals(numberOfThreads, updatedProduct.getReviewCount());
        assertEquals(5.0f, updatedProduct.getAverageScore(), 0.01);
    }
}
