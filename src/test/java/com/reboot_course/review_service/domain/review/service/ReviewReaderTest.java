package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.exception.ProductNotFoundException;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.review.dto.ReviewListResponse;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
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

        Member member = memberRepository.save(new Member());

        insertedReviews = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            Review review = Review.builder()
                    .product(product)
                    .member(member)
                    .score(random.nextInt(5) + 1)
                    .content("Review content " + i)
                    .imageUrl(i % 2 == 0 ? "/images/review" + i + ".jpg" : null)
                    .createdAt(LocalDateTime.now().minusHours(i))  // 시간 차이를 두어 정렬 테스트
                    .build();
            insertedReviews.add(reviewRepository.save(review));
        }

        insertedReviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
    }

    @Test
    @DisplayName("성공 테스트: 50개의 데이터에 대한 커서 기반 페이지네이션")
    void t1() {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);
        Long cursor = null;

        for (int page = 0; page < 5; page++) {
            ReviewListResponse response = reviewReader.readReviews(productId, cursor, pageable);

            System.out.println("======================");
            response.reviews().forEach(r-> System.out.println(r.createdAt()));
            System.out.println("======================");

            assertEquals(50, response.totalCount());
            assertEquals(3.5F, response.score());
            assertEquals(pageSize, response.reviews().size());

            // 정렬 검증 (최신순)
            for (int i = 0; i < pageSize - 1; i++) {
                assertTrue(response.reviews().get(i).createdAt()
                        .isAfter(response.reviews().get(i + 1).createdAt()));
            }

            // 페이지 연속성 검증
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
    @DisplayName("성공 케이스: 마지막 페이지 이후 테스트")
    void t2() {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);
        Long cursor = null;
        ReviewListResponse lastPageResponse;

        // 모든 페이지를 순회
        for (int page = 0; page < 5; page++) {
            lastPageResponse = reviewReader.readReviews(productId, cursor, pageable);
            cursor = lastPageResponse.cursor();
        }

        // 마지막 페이지 이후 추가 요청
        ReviewListResponse emptyResponse = reviewReader.readReviews(productId, cursor, pageable);

        // 검증
        assertTrue(emptyResponse.reviews().isEmpty(), "마지막 페이지 이후에는 빈 리스트가 반환되어야 합니다.");
        assertEquals(50, emptyResponse.totalCount(), "총 리뷰 수는 여전히 50이어야 합니다.");
        assertEquals(3.5F, emptyResponse.score(), "평균 점수는 여전히 3.5여야 합니다.");
        assertNull(emptyResponse.cursor(), "빈 리스트에서도 커서는 제공되어야 합니다.");
    }

    @Test
    @DisplayName("실패 테스트: 상품이 없는 경우, 예외 반환")
    void t3() {
        Long nonExistentProductId = 9999L;
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(ProductNotFoundException.class, () -> {
            reviewReader.readReviews(nonExistentProductId, null, pageable);
        });
    }
}