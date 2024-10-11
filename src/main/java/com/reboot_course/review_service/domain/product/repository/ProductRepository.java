package com.reboot_course.review_service.domain.product.repository;

import com.reboot_course.review_service.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT p FROM Product p WHERE p.id = :id")
//    Optional<Product> findByIdWithPessimisticLock(@Param("id") Long id);
}