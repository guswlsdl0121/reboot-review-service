package com.reboot_course.review_service.service;

import com.reboot_course.review_service.dto.request.ReviewCreateRequest;
import com.reboot_course.review_service.entity.Product;
import com.reboot_course.review_service.entity.Review;
import com.reboot_course.review_service.entity.User;
import com.reboot_course.review_service.implement.product.ProductFinder;
import com.reboot_course.review_service.implement.review.ReviewAppender;
import com.reboot_course.review_service.implement.user.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserFinder userFinder;
    private final ProductFinder productFinder;
    private final ReviewAppender reviewAppender;
    private final ImageManager imageManager;

    public Long create(Long productId, ReviewCreateRequest request, MultipartFile image) {
        User user = userFinder.fetch(request.userId());
        Product product = productFinder.fetch(productId);
        String imageUrl = imageManager.upload(image);
        Review review = reviewAppender.append(request, user, product, imageUrl);

        return review.getId();
    }
}