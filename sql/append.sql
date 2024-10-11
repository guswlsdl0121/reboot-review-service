-- 새 사용자 추가 (ID가 1001인 사용자)
INSERT INTO member (id) VALUES (1002);

-- 새 상품 추가 (ID가 101인 상품)
INSERT INTO product (id, review_count, score) VALUES (101, 0, 0.0);

-- 1. 특정 사용자가 특정 상품에 대한 리뷰를 작성했는지 확인
SELECT id
FROM review
WHERE member_id = 1001
  AND product_id = 101
LIMIT 1;

-- 2. 특정 사용자의 존재 여부 확인
SELECT id
FROM member
WHERE id = 1001;

-- 3. 특정 상품 정보 조회 (FOR UPDATE로 잠금)
SELECT id, score, review_count
FROM product
WHERE id = 101
    FOR UPDATE;

-- 4. 새로운 리뷰 삽입
INSERT INTO review
(content, created_at, image_url, member_id, product_id, score)
VALUES
    ('This is a great product!', NOW(), NULL, 1001, 101, 4);

-- 5. 상품의 평균 점수와 리뷰 개수 업데이트
UPDATE product
SET score = 4.0, review_count = 1
WHERE id = 101;