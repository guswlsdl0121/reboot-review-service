package com.reboot_course.review_service.domain.review.repository;

import com.reboot_course.review_service.domain.review.entity.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    List<Review> findAllByProductId(Long productId);
}
