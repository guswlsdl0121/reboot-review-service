package com.reboot_course.review_service.service;

import com.reboot_course.review_service.dto.request.ReviewCreateRequest;
import com.reboot_course.review_service.entity.Product;
import com.reboot_course.review_service.entity.Review;
import com.reboot_course.review_service.entity.User;
import com.reboot_course.review_service.repository.ProductRepository;
import com.reboot_course.review_service.repository.ReviewRepository;
import com.reboot_course.review_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;

    @Transactional
    public Long create(Long productId, ReviewCreateRequest request, MultipartFile image) throws IOException {
        Review newReview = Review.builder()
                .score(request.score())
                .content(request.content())
                .imageUrl(imageService.upload(image))
                .product(fetchProduct(productId))
                .user(fetchUser(request.userId()))
                .build();

        reviewRepository.save(newReview);
        return newReview.getId();
    }

    private Product fetchProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
