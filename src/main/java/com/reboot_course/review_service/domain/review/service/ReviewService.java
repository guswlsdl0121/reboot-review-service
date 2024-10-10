package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.dto.ReviewListResponse;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.infrastructure.image.ImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewValidator reviewValidator;
    private final ReviewAppender reviewAppender;
    private final ImageManager imageManager;

    public Long create(Long productId, ReviewCreateRequest request, MultipartFile image) {
        reviewValidator.validate(productId, request);
        String imageUrl = imageManager.upload(image);
        Review review = reviewAppender.append(productId, request, imageUrl);

        return review.getId();
    }

    public ReviewListResponse readByPagination(Long productId, Long cursor, int size) {
        return null;
    }
}