package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ReviewAppenderTest {

    @Autowired
    private ReviewAppender reviewAppender;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long productId;
    private List<Long> userIds;

    @BeforeEach
    void setUp() {
        userIds = IntStream.range(0, 5)
                .mapToObj(i -> memberRepository.save(new Member()))
                .map(Member::getId)
                .toList();

        Product product = productRepository.save(Product.builder()
                .reviewCount(0L)
                .averageScore(0F)
                .build());

        productId = product.getId();
    }

    @ParameterizedTest
    @MethodSource("reviewDataProvider")
    @DisplayName("5명의 사용자가 1개의 상품에 대해 리뷰를 모두 넣는 테스트")
    void t1(List<ReviewTestData> reviewDataList) {
        float expectedTotalScore = 0;
        for (int i = 0; i < reviewDataList.size(); i++) {
            ReviewTestData data = reviewDataList.get(i);
            Long userId = userIds.get(i);

            ReviewCreateRequest request = new ReviewCreateRequest(userId, data.score(), data.content());
            reviewAppender.append(productId, request, data.imageUrl());

            expectedTotalScore += data.score();
        }

        // 최종 상태 확인
        List<Review> productReviews = reviewRepository.findAllByProductId(productId);
        assertEquals(reviewDataList.size(), productReviews.size());

        Product finalProduct = productRepository.findById(productId).orElseThrow();
        assertEquals(reviewDataList.size(), finalProduct.getReviewCount());
        assertEquals(expectedTotalScore / reviewDataList.size(), finalProduct.getAverageScore(), 0.001);

        // 각 리뷰의 세부 사항 확인
        for (int i = 0; i < reviewDataList.size(); i++) {
            Review review = productReviews.get(i);
            ReviewTestData data = reviewDataList.get(i);

            assertEquals(userIds.get(i), review.getMember().getId());
            assertEquals(productId, review.getProduct().getId());
            assertEquals(data.score(), review.getScore());
            assertEquals(data.content(), review.getContent());
            assertEquals(data.imageUrl(), review.getImageUrl());
        }
    }

    static Stream<List<ReviewTestData>> reviewDataProvider() {
        return Stream.of(
                List.of(
                        new ReviewTestData(5, "Great product!", "/images/review1.jpg"),
                        new ReviewTestData(4, "Good product", ""),
                        new ReviewTestData(3, "Average product", "/images/review2.jpg"),
                        new ReviewTestData(2, "Not so good", ""),
                        new ReviewTestData(1, "Bad product", "/images/review3.jpg")
                )
        );
    }

    record ReviewTestData(int score, String content, String imageUrl) {}
}