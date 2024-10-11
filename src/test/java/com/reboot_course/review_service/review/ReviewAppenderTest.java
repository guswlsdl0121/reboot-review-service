package com.reboot_course.review_service.review;

import com.reboot_course.review_service.config.ReviewTestConfig;
import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import com.reboot_course.review_service.domain.review.service.ReviewAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Import(ReviewTestConfig.class)
class ReviewAppenderTest {

    @Autowired
    private ReviewAppender reviewAppender;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Product testProduct;
    private List<Member> testMembers;

    @BeforeEach
    void setUp() {
        testProduct = productRepository.save(Product.builder()
                .reviewCount(0L)
                .averageScore(0F)
                .build());

        testMembers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testMembers.add(memberRepository.save(new Member()));
        }
    }

    @Test
    @DisplayName("단일 리뷰 추가 시 리뷰와 상품 정보가 정상적으로 업데이트되어야 한다")
    void t1() {
        // Given
        ReviewCreateRequest request = new ReviewCreateRequest(testMembers.get(0).getId(), 5, "Great product!");

        // When
        Review review = reviewAppender.append(testProduct.getId(), request, "image_url.jpg");

        // Then
        assertNotNull(review);
        assertEquals(testMembers.get(0).getId(), review.getMember().getId());
        assertEquals(testProduct.getId(), review.getProduct().getId());
        assertEquals(5, review.getScore());
        assertEquals("Great product!", review.getContent());
        assertEquals("image_url.jpg", review.getImageUrl());

        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        assertEquals(1L, updatedProduct.getReviewCount());
        assertEquals(5F, updatedProduct.getAverageScore());
    }

    @Test
    @DisplayName("여러 개의 리뷰 추가 시 리뷰 개수와 평균 점수가 정확하게 계산되어야 한다")
    void t2() {
        // Given
        int reviewCount = 5;
        float expectedTotalScore = 0;

        // When
        for (int i = 0; i < reviewCount; i++) {
            int score = i + 1;
            ReviewCreateRequest request = new ReviewCreateRequest(testMembers.get(i).getId(), score, "Review " + i);
            reviewAppender.append(testProduct.getId(), request, "image_url_" + i + ".jpg");
            expectedTotalScore += score;
        }

        // Then
        List<Review> reviews = reviewRepository.findAllByProductId(testProduct.getId());
        assertEquals(reviewCount, reviews.size());

        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();
        assertEquals(reviewCount, updatedProduct.getReviewCount());
        assertEquals(expectedTotalScore / reviewCount, updatedProduct.getAverageScore(), 0.01);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("동시에 여러 리뷰가 추가될 때 리뷰 개수와 평균 점수가 정확하게 계산되어야 한다")
    void t3() throws InterruptedException {
        // Given
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // When
        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    ReviewCreateRequest request = new ReviewCreateRequest(
                            testMembers.get(finalI).getId(),
                            5,
                            "Concurrent review " + finalI);
                    reviewAppender.append(testProduct.getId(), request, null);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // Then
        Product updatedProduct = productRepository.findById(testProduct.getId()).orElseThrow();

        assertEquals(numberOfThreads, updatedProduct.getReviewCount());
        assertEquals(5.0f, updatedProduct.getAverageScore(), 0.01);

        List<Review> reviews = reviewRepository.findAllByProductId(testProduct.getId());
        assertEquals(numberOfThreads, reviews.size());
    }
}