drop database if exists review_service_db;
create database review_service_db;
use review_service_db;

-- Member 테이블 생성
CREATE TABLE IF NOT EXISTS `member`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARSET = utf8;

-- Product 테이블 생성
CREATE TABLE IF NOT EXISTS `product`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT,
    `review_count` BIGINT    NOT NULL,
    `score`        FLOAT(23) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARSET = utf8;

-- Review 테이블 생성
CREATE TABLE IF NOT EXISTS `review`
(
    `id`         BIGINT        NOT NULL AUTO_INCREMENT,
    `score`      TINYINT       NOT NULL CHECK ((score <= 5) AND (score >= 1)),
    `created_at` DATETIME(6)   NOT NULL,
    `member_id`  BIGINT        NOT NULL,
    `product_id` BIGINT        NOT NULL,
    `content`    VARCHAR(1000) NOT NULL,
    `image_url`  VARCHAR(255),
    PRIMARY KEY (`id`),
    INDEX `idx_product_created_at` (`product_id`, `created_at`),
    INDEX `idx_product_id` (`product_id`),
    CONSTRAINT `FK_review_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `FK_review_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE = InnoDB
  CHARSET = utf8;