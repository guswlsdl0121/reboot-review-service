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

    @Transient
    private Float totalScore;

    @PostLoad
    private void calculateTotalScore() {
        this.totalScore = this.averageScore * this.reviewCount;
    }

    public void updateStat(int score) {
        this.totalScore = (this.totalScore == null) ? 0 : this.totalScore;
        this.totalScore += score;
        this.reviewCount++;
        this.averageScore = this.totalScore / this.reviewCount;
    }
}
