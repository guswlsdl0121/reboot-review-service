package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.common.constant.GlobalConstants;
import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
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
        validateUniqueReview(request.memberId(), productId);
        validateScoreRange(request.score());
    }

    private void validateUniqueReview(Long memberId, Long productId) {
        if (reviewRepository.existsByMember_IdAndProductId(memberId, productId)) {
            throw new DuplicateReviewException(memberId, productId);
        }
    }

    private void validateScoreRange(Integer score) {
        if (score == null || score < GlobalConstants.MIN_SCORE || score > GlobalConstants.MAX_SCORE) {
            throw new InvalidScoreException();
        }
    }
}