package com.reboot_course.review_service.domain.review.entity;


import com.reboot_course.review_service.common.constant.GlobalConstants;
import com.reboot_course.review_service.domain.member.entity.Member;
import com.reboot_course.review_service.domain.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_member_product", columnNames = {"member_id", "product_id"})
        },
        indexes = {
                @Index(name = "idx_product_created_at", columnList = "product_id, created_at"),
                @Index(name = "idx_product_id", columnList = "product_id"),
                @Index(name = "idx_member_product", columnList = "member_id, product_id")
        }
)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TINYINT")
    @Min(GlobalConstants.MIN_SCORE)
    @Max(GlobalConstants.MAX_SCORE)
    private Integer score;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column
    private String imageUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Long getMemberId() {
        return member.getId();
    }
}