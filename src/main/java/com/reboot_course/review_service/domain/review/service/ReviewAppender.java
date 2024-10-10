package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.service.ProductUpdater;
import com.reboot_course.review_service.domain.review.dto.request.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import com.reboot_course.review_service.domain.user.entity.User;
import com.reboot_course.review_service.domain.user.service.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewAppender {
    private final ReviewRepository reviewRepository;
    private final UserFinder userFinder;
    private final ProductUpdater productUpdater;

    @Transactional
    public Review append(Long productId, ReviewCreateRequest request, String imageUrl) {
        User user = userFinder.fetch(request.userId());
        Product updatedProduct = productUpdater.getOneAndUpdateStat(productId, request.score());

        Review review = Review.builder()
                .user(user)
                .product(updatedProduct)
                .score(request.score())
                .content(request.content())
                .imageUrl(imageUrl)
                .build();

        return reviewRepository.save(review);
    }
}