package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.review.dto.request.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.exception.DuplicateReviewException;
import com.reboot_course.review_service.domain.review.exception.InvalidScoreException;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewValidator {
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public void validate(Long productId, ReviewCreateRequest request) {
        validateUniqueReview(request.userId(), productId);
        validateScoreRange(request.score());
    }

    private void validateUniqueReview(Long userId, Long productId) {
        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new DuplicateReviewException(userId, productId);
        }
    }

    private void validateScoreRange(Integer score) {
        if (score == null || score < 1 || score > 5) {
            throw new InvalidScoreException();
        }
    }
}