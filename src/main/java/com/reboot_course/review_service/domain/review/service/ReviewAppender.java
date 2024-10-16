package com.reboot_course.review_service.domain.review.service;

import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.member.service.MemberFinder;
import com.reboot_course.review_service.domain.product.entity.Product;
import com.reboot_course.review_service.domain.product.service.ProductFinder;
import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.entity.Review;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewAppender {
    private final ReviewRepository reviewRepository;
    private final MemberFinder memberFinder;
    private final ProductFinder productFinder;

    @Transactional
    public Review append(Long productId, ReviewCreateRequest request, String imageUrl) {
        Member member = memberFinder.fetchOne(request.memberId());
        Product updatedProduct = productFinder.fetchOneAndUpdateStat(productId, request.score());

        Review review = Review.builder()
                .member(member)
                .product(updatedProduct)
                .score(request.score())
                .content(request.content())
                .imageUrl(imageUrl)
                .build();

        return reviewRepository.save(review);
    }
}