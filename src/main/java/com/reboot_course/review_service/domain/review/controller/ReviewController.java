package com.reboot_course.review_service.domain.review.controller;

import com.reboot_course.review_service.domain.review.dto.ReviewCreateRequest;
import com.reboot_course.review_service.domain.review.dto.ReviewListResponse;
import com.reboot_course.review_service.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Void> createReview(
            @PathVariable Long productId,
            @RequestPart("review") ReviewCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Long reviewId = reviewService.create(productId, request, image);

        URI resourceURI = URI.create("/products/" + productId + "/reviews/" + reviewId);
        return ResponseEntity.created(resourceURI).build();
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ReviewListResponse> getReviews(
            @PathVariable Long productId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        ReviewListResponse response = reviewService.readByPagination(productId, cursor, size);
        return ResponseEntity.ok(response);
    }
}