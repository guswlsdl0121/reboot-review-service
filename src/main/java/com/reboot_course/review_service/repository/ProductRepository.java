package com.reboot_course.review_service.repository;

import com.reboot_course.review_service.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
