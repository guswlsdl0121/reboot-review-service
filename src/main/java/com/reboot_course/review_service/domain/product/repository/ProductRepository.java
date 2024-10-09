package com.reboot_course.review_service.domain.product.repository;

import com.reboot_course.review_service.domain.product.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
