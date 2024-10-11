package com.reboot_course.review_service.review;

import com.reboot_course.review_service.config.ReviewTestConfig;
import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.exception.ProductNotFoundException;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.review.dto.ReviewListResponse;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import com.reboot_course.review_service.domain.review.service.ReviewReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(ReviewTestConfig.class)
class ReviewReaderTest {

    @Autowired
    private ReviewReader reviewReader;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long productId;
    private List<Review> insertedReviews;

    @BeforeEach
    void setUp() {
        Product product = productRepository.save(Product.builder()
                .reviewCount(50L)
                .averageScore(3.5F)
                .build());
        productId = product.getId();

        insertedReviews = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            Member member = memberRepository.save(new Member());
            Review review = Review.builder()
                    .product(product)
                    .member(member)
                    .score(random.nextInt(5) + 1)
                    .content("Review content " + i)
                    .imageUrl(i % 2 == 0 ? "/images/review" + i + ".jpg" : null)
                    .createdAt(LocalDateTime.now().minusHours(i))
                    .build();
            insertedReviews.add(reviewRepository.save(review));
        }

        insertedReviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
    }

    @Test
    @DisplayName("커서 기반 페이지네이션으로 50개의 리뷰를 10개씩 조회할 때 모든 페이지가 올바르게 반환되어야 한다")
    void t1() {
        // Given
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);
        Long cursor = null;

        for (int page = 0; page < 5; page++) {
            // When
            ReviewListResponse response = reviewReader.readReviews(productId, cursor, pageable);

            // Then
            assertEquals(50, response.totalCount());
            assertEquals(3.5F, response.score());
            assertEquals(pageSize, response.reviews().size());

            // 정렬 검증 (최신순)
            for (int i = 0; i < pageSize - 1; i++) {
                assertTrue(response.reviews().get(i).createdAt()
                        .isAfter(response.reviews().get(i + 1).createdAt()));
            }

            assertNotNull(response.cursor());

            // 첫 페이지와 마지막 페이지 검증
            if (page == 0) {
                assertEquals(insertedReviews.get(0).getCreatedAt(), response.reviews().get(0).createdAt());
            } else if (page == 4) {
                assertEquals(insertedReviews.get(49).getCreatedAt(), response.reviews().get(9).createdAt());
            }

            cursor = response.cursor();
        }
    }

    @Test
    @DisplayName("마지막 페이지 이후 조회 시 reviews는 빈 리스트, cursur는 null이 반환되어야 한다.")
    void t2() {
        // Given
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);
        Long cursor = null;
        ReviewListResponse lastPageResponse;

        // When
        for (int page = 0; page < 5; page++) {
            lastPageResponse = reviewReader.readReviews(productId, cursor, pageable);
            cursor = lastPageResponse.cursor();
        }
        ReviewListResponse emptyResponse = reviewReader.readReviews(productId, cursor, pageable);

        // Then
        assertTrue(emptyResponse.reviews().isEmpty(), "마지막 페이지 이후에는 빈 리스트가 반환되어야 합니다.");
        assertEquals(50, emptyResponse.totalCount(), "총 리뷰 수는 여전히 50이어야 합니다.");
        assertEquals(3.5F, emptyResponse.score(), "평균 점수는 여전히 3.5여야 합니다.");
        assertNull(emptyResponse.cursor(), "빈 리스트에서는 커서가 null이어야 합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회 시 ProductNotFoundException이 발생해야 한다")
    void t3() {
        // Given
        Long nonExistentProductId = 9999L;
        Pageable pageable = PageRequest.of(0, 10);

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            reviewReader.readReviews(nonExistentProductId, null, pageable);
        });
    }
}