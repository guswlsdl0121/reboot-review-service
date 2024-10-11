-- 상품 정보 조회
SELECT id, score, review_count
FROM product
WHERE id = 1;

-- 특정 리뷰 조회
SELECT id, content, created_at, image_url, member_id, product_id, score
FROM review
WHERE id = 33571;

-- 페이지네이션을 위한 리뷰 목록 조회
SELECT id, content, created_at, image_url, member_id, product_id, score
FROM review
WHERE product_id = 1
  AND created_at < '2024-09-29 06:00:32.811'
ORDER BY created_at DESC
LIMIT 10;

-- 특정 상품의 총 리뷰 수 조회
SELECT COUNT(id)
FROM review
WHERE product_id = 1;

-- EXPLAIN 쿼리 실행
EXPLAIN
SELECT id, content, created_at, image_url, member_id, product_id, score
FROM review
WHERE product_id = 1
  AND created_at < '2024-10-11 06:50:26.370'
ORDER BY created_at DESC
LIMIT 10;