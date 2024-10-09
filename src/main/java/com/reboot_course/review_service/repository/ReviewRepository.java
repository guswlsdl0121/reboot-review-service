package com.reboot_course.review_service.repository;

import com.reboot_course.review_service.entity.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
