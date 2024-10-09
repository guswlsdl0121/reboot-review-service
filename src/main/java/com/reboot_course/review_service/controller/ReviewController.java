package com.reboot_course.review_service.controller;

import com.reboot_course.review_service.dto.request.ReviewCreateRequest;
import com.reboot_course.review_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        Long reviewId = reviewService.create(productId, request, image);
        URI resourceURI = URI.create("/products/" + productId + "/reviews/" + reviewId);
        return ResponseEntity.created(resourceURI).build();
    }
}