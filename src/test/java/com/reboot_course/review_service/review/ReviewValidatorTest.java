package com.reboot_course.review_service.review;

import com.reboot_course.review_service.common.constant.GlobalConstants;
import com.reboot_course.review_service.config.ReviewTestConfig;
import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.exception.DuplicateReviewException;
import com.reboot_course.review_service.domain.review.exception.InvalidScoreException;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import com.reboot_course.review_service.domain.review.service.ReviewValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@Import(ReviewTestConfig.class)
class ReviewValidatorTest {

    @Autowired
    private ReviewValidator reviewValidator;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testMember = memberRepository.save(new Member());
        testProduct = productRepository.save(Product.builder()
                .reviewCount(0L)
                .averageScore(0F)
                .build());
    }

    @Test
    @DisplayName("유효한 리뷰 요청 시 예외가 발생하지 않아야 한다")
    void testValidReviewRequest() {
        // Given
        ReviewCreateRequest validRequest = new ReviewCreateRequest(testMember.getId(), 3, "Valid content");

        // When & Then
        assertDoesNotThrow(() -> reviewValidator.validate(testProduct.getId(), validRequest));
    }

    @Test
    @DisplayName("중복 리뷰 요청 시 DuplicateReviewException이 발생해야 한다")
    void testDuplicateReviewRequest() {
        // Given
        Review existingReview = Review.builder()
                .member(testMember)
                .product(testProduct)
                .score(3)
                .content("Existing review")
                .build();
        reviewRepository.save(existingReview);

        ReviewCreateRequest duplicateRequest = new ReviewCreateRequest(testMember.getId(), 4, "Duplicate review");

        // When & Then
        assertThrows(DuplicateReviewException.class, () -> reviewValidator.validate(testProduct.getId(), duplicateRequest));
    }

    @Test
    @DisplayName("최소 점수 미만으로 요청 시 InvalidScoreException이 발생해야 한다")
    void testScoreBelowMinimum() {
        // Given
        ReviewCreateRequest invalidRequest = new ReviewCreateRequest(testMember.getId(), GlobalConstants.MIN_SCORE - 1, "Invalid score");

        // When & Then
        assertThrows(InvalidScoreException.class, () -> reviewValidator.validate(testProduct.getId(), invalidRequest));
    }

    @Test
    @DisplayName("최대 점수 초과로 요청 시 InvalidScoreException이 발생해야 한다")
    void testScoreAboveMaximum() {
        // Given
        ReviewCreateRequest invalidRequest = new ReviewCreateRequest(testMember.getId(), GlobalConstants.MAX_SCORE + 1, "Invalid score");

        // When & Then
        assertThrows(InvalidScoreException.class, () -> reviewValidator.validate(testProduct.getId(), invalidRequest));
    }

    @Test
    @DisplayName("NULL 점수로 요청 시 InvalidScoreException이 발생해야 한다")
    void testNullScore() {
        // Given
        ReviewCreateRequest invalidRequest = new ReviewCreateRequest(testMember.getId(), null, "Invalid score");

        // When & Then
        assertThrows(InvalidScoreException.class, () -> reviewValidator.validate(testProduct.getId(), invalidRequest));
    }
}