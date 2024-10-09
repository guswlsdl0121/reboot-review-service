## 리뷰 서비스 스펙 및 제약 조건

## 설명
- 사전에 주어진 테이블, API 스펙 등을 정리한다.

<br>

## product 테이블
```sql
CREATE TABLE IF NOT EXISTS `Product` (
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `reviewCount` BIGINT(20) NOT NULL,
    `score`       FLOAT  NOT NULL
) ENGINE = InnoDB CHARSET = utf8;
```

<br>

## API 스펙
### 1. 리뷰 조회 API

#### Request URL
```http request
GET  /products/{productId}/reviews?cursor={cursor}&size={size}
```

#### Request Param
- productId : 상품 아이디
- cursor : 커서 값 (직전 조회 API 의 응답으로 받은 cursor 값)
- size : 조회 사이즈 (default = 10)

#### Response Body
```json
{
    "totalCount": 15, // 해당 상품에 작성된 총리뷰 수
    "score": 4.6, // 평균 점수
    "cursor": 6,
    "reviews": [
    {
        "id": 15,
        "userId": 1, // 작성자 유저 아이디
        "score": 5,
        "content": "이걸 사용하고 제 인생이 달라졌습니다.",
        "imageUrl": "/image.png",
        "createdAt": "2024-11-25T00:00:00.000Z"
    },
    {
        "id": 14,
        "userId": 3, // 작성자 유저 아이디
        "score": 5,
        "content": "이걸 사용하고 제 인생이 달라졌습니다.",
        "imageUrl": null,
        "createdAt": "2024-11-24T00:00:00.000Z"
    }
    ]
}
```

<br>

### 2. 리뷰 등록 API
#### Request URL
```http request
POST  /products/{productId}/reviews
```

#### Request Part
- `Multi-Part file` : 단건의 이미지

#### Request Body
```json
{
  "userId": 1,
  "score": 4,
  "content": "이걸 사용하고 제 인생이 달라졌습니다.",
}
```

<br>

### 3. 주의 사항
- 사용자 회원 가입 / 로그인은 따로 구현하지 않는다.
- 리뷰 등록 API에서 사용하는 **userId에 해당하는 사용자는 항상 존재한다고 가정**한다.