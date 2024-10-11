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

    @Version
    private Long version = 0L;

    public void updateStat(int score) {
        float totalScore = this.averageScore * this.reviewCount;
        totalScore += score;
        this.reviewCount++;
        this.averageScore = totalScore / this.reviewCount;
    }
}