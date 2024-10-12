# 리뷰 서비스

### 프로젝트 개요

이 프로젝트는 상품에 대한 리뷰를 작성하고 관리하는 서비스이다.

사용자는 상품에 대한 리뷰를 작성할 수 있으며, 각 상품에 대한 리뷰들도 조회할 수 있다.

### 주요 기능
- 상품에 대한 리뷰를 작성
- 상품별 최신순 리뷰 조회

### 기술 스택
- Java
- Spring Boot
- MySQL
- Docker
- JPA

### 실행 방법

1. Git clone
    ```
    git clone https://github.com/guswlsdl0121/reboot-review-service.git
    ```
2. 디렉토리 이동

    ```
    cd [project directory]/docker
    ```

3. 도커 실행
    ``` 
    docker-compose up --build
    ```

### API 엔드포인트

1. 리뷰 조회 API
    ```
    GET /products/{productId}/reviews?cursor={cursor}&size={size}
    ```

2. 리뷰 등록 API
    ```
    POST /products/{productId}/reviews
    ```

## 주요 구현 사항

- MySQL 테이블에 인덱스 적용하여 성능 최적화
- 동시성 문제 해결을 위한 낙관적 락 적용