package com.reboot_course.review_service.config;

import com.reboot_course.review_service.domain.member.repository.MemberRepository;
import com.reboot_course.review_service.domain.member.service.MemberFinder;
import com.reboot_course.review_service.domain.product.repository.ProductRepository;
import com.reboot_course.review_service.domain.product.service.ProductFinder;
import com.reboot_course.review_service.domain.review.repository.ReviewRepository;
import com.reboot_course.review_service.domain.review.service.ReviewAppender;
import com.reboot_course.review_service.domain.review.service.ReviewReader;
import com.reboot_course.review_service.domain.review.service.ReviewValidator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ReviewTestConfig {

    @Bean
    public ReviewAppender reviewAppender(ReviewRepository reviewRepository,
                                         MemberFinder memberFinder,
                                         ProductFinder productFinder) {
        return new ReviewAppender(reviewRepository, memberFinder, productFinder);
    }

    @Bean
    public ReviewReader reviewReader(ReviewRepository reviewRepository,
                                     ProductFinder productFinder) {
        return new ReviewReader(reviewRepository, productFinder);
    }

    @Bean
    public ReviewValidator reviewValidator(ReviewRepository reviewRepository) {
        return new ReviewValidator(reviewRepository);
    }

    @Bean
    public MemberFinder memberFinder(MemberRepository memberRepository) {
        return new MemberFinder(memberRepository);
    }

    @Bean
    public ProductFinder productFinder(ProductRepository productRepository) {
        return new ProductFinder(productRepository);
    }
}