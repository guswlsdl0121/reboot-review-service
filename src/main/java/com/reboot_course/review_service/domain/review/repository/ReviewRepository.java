package com.reboot_course.review_service.domain.review.repository;

import com.reboot_course.review_service.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMember_IdAndProductId(Long memberId, Long productId);

    List<Review> findAllByProductId(Long productId);

    Long countByProductId(Long productId);

    List<Review> findByProductIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            Long productId, LocalDateTime createdAt, Pageable pageable);

    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);
}