package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.service.ProductFinder;
import com.reboot_course.review_service.domain.review.dto.ReviewListResponse;
import com.reboot_course.review_service.domain.review.dto.ReviewResponse;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.exception.InvalidCursorException;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewReader {
    private final ReviewRepository reviewRepository;
    private final ProductFinder productFinder;

    @Transactional(readOnly = true)
    public ReviewListResponse readReviews(Long productId, Long cursor, Pageable pageable) {
        Product product = productFinder.fetchOne(productId);

        List<Review> reviews;
        if (cursor != null) {
            Review cursorReview = reviewRepository.findById(cursor)
                    .orElseThrow(() -> new InvalidCursorException(cursor));

            reviews = reviewRepository.findByProductIdAndCreatedAtBeforeOrderByCreatedAtDesc(
                    productId, cursorReview.getCreatedAt(), pageable
            );

        } else {
            reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable);
        }

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(this::mapToReviewResponse)
                .toList();

        Long totalCount = reviewRepository.countByProductId(productId);
        Long nextCursor = getNextCursor(reviews);

        return new ReviewListResponse(totalCount, product.getAverageScore(), nextCursor, reviewResponses);
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getMemberId(),
                review.getScore(),
                review.getContent(),
                review.getImageUrl(),
                review.getCreatedAt()
        );
    }

    private Long getNextCursor(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return null;
        }

        return reviews.get(reviews.size() - 1).getId();
    }
}