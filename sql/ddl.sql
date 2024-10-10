drop database review_service_db;
create database review_service_db;
use review_service_db;

-- Member 테이블 생성
CREATE TABLE IF NOT EXISTS `member`
(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY
) ENGINE = InnoDB
  CHARSET = utf8;

-- Product 테이블 생성
CREATE TABLE IF NOT EXISTS `product`
(
    `id`           BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `review_count` BIGINT(20) NOT NULL,
    `score`        FLOAT      NOT NULL
) ENGINE = InnoDB
  CHARSET = utf8;

-- Review 테이블 생성
CREATE TABLE IF NOT EXISTS `review`
(
    `id`         BIGINT(20)    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT(20)    NOT NULL,
    `member_id`    BIGINT(20)    NOT NULL,
    `score`      TINYINT       NOT NULL,
    `content`    VARCHAR(1000) NOT NULL,
    `image_url`  VARCHAR(255),
    `created_at` DATETIME(6)   NOT NULL,
    CONSTRAINT `fk_review_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    CONSTRAINT `fk_review_user` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    INDEX `idx_review_product_id` (`product_id`),
    INDEX `idx_review_user_id` (`member_id`)
) ENGINE = InnoDB
  CHARSET = utf8;