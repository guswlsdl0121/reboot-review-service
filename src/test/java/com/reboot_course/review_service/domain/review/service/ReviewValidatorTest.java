package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.common.constant.GlobalConstants;
import com.reboot_course.review_service.domain.review.dto.request.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.exception.DuplicateReviewException;
import com.reboot_course.review_service.domain.review.exception.InvalidScoreException;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewValidatorTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewValidator reviewValidator;

    private ReviewCreateRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new ReviewCreateRequest(1L, 3, "Valid content");
    }

    @Test
    @DisplayName("유효한 요청으로 검증 시 예외가 발생하지 않음")
    void t1() {
        when(reviewRepository.existsByUserIdAndProductId(anyLong(), anyLong())).thenReturn(false);

        assertDoesNotThrow(() -> reviewValidator.validate(1L, validRequest));
    }

    @Test
    @DisplayName("중복 리뷰 요청 시 DuplicateReviewException 발생")
    void t2() {
        when(reviewRepository.existsByUserIdAndProductId(anyLong(), anyLong())).thenReturn(true);

        assertThrows(DuplicateReviewException.class, () -> reviewValidator.validate(1L, validRequest));
    }

    @Test
    @DisplayName("최소 점수 미만으로 요청 시 InvalidScoreException 발생")
    void t3() {
        ReviewCreateRequest invalidRequest = new ReviewCreateRequest(1L, GlobalConstants.MIN_SCORE - 1, "Invalid score");

        assertThrows(InvalidScoreException.class, () -> reviewValidator.validate(1L, invalidRequest));
    }

    @Test
    @DisplayName("최대 점수 초과로 요청 시 InvalidScoreException 발생")
    void t4() {
        ReviewCreateRequest invalidRequest = new ReviewCreateRequest(1L, GlobalConstants.MAX_SCORE + 1, "Invalid score");

        assertThrows(InvalidScoreException.class, () -> reviewValidator.validate(1L, invalidRequest));
    }

    @Test
    @DisplayName("NULL 점수로 요청 시 InvalidScoreException 발생")
    void t5() {
        ReviewCreateRequest invalidRequest = new ReviewCreateRequest(1L, null, "Invalid score");

        assertThrows(InvalidScoreException.class, () -> reviewValidator.validate(1L, invalidRequest));
    }
}