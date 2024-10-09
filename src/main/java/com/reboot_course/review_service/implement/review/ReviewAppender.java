package com.reboot_course.review_service.implement.review;

import com.reboot_course.review_service.dto.request.ReviewCreateRequest;
import com.reboot_course.review_service.entity.Product;
import com.reboot_course.review_service.entity.Review;
import com.reboot_course.review_service.entity.User;
import com.reboot_course.review_service.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewAppender {
    private final ReviewRepository reviewRepository;

    @Transactional
    public Review append(ReviewCreateRequest request, User user, Product product, String imageUrl) {
        Review review = Review.builder()
                .user(user)
                .product(product)
                .score(request.score())
                .content(request.content())
                .imageUrl(imageUrl)
                .build();

        return reviewRepository.save(review);
    }
}