package com.reboot_course.review_service.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewCount;

    @Column(name = "score", nullable = false)
    private Float averageScore;

    public void updateReviewCountAndScore(int score) {
        float currentTotalScore = this.averageScore * this.reviewCount;
        this.reviewCount++;
        float newTotalScore = currentTotalScore + score;
        this.averageScore = newTotalScore / this.reviewCount;
    }
}