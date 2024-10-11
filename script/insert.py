import mysql.connector
from mysql.connector import Error
from datetime import datetime, timedelta
import random
import string

def create_connection():
    try:
        connection = mysql.connector.connect(
            host='localhost',
            port=3307,
            database='review_service_db',
            user='reboot',
            password='reboot1234'
        )
        if connection.is_connected():
            return connection
    except Error as e:
        print(f"Error while connecting to MySQL: {e}")
    return None

def insert_dummy_data(connection):
    try:
        cursor = connection.cursor()

        # Insert dummy members
        member_count = 1000
        cursor.executemany("INSERT INTO `member` () VALUES ()", [() for _ in range(member_count)])
        print(f"Inserted {member_count} members")

        # Insert dummy products
        product_count = 100
        cursor.executemany("INSERT INTO `product` (review_count, score) VALUES (0, 0)", [() for _ in range(product_count)])
        print(f"Inserted {product_count} products")

        # Prepare review insert query
        review_insert_query = """
        INSERT INTO `review` 
        (product_id, member_id, score, content, image_url, created_at) 
        VALUES (%s, %s, %s, %s, %s, %s)
        """

        # Generate unique product-member combinations
        product_member_combinations = set()
        while len(product_member_combinations) < 50000:
            product_id = random.randint(1, product_count)
            member_id = random.randint(1, member_count)
            combination = (product_id, member_id)
            if combination not in product_member_combinations:
                product_member_combinations.add(combination)

        # Generate and insert 50,000 reviews
        reviews = []
        start_date = datetime.now() - timedelta(days=365)
        for product_id, member_id in product_member_combinations:
            score = random.randint(1, 5)
            content = ''.join(random.choices(string.ascii_letters + string.digits, k=random.randint(10, 1000)))
            image_url = f"https://example.com/images/{random.randint(1, 1000)}.jpg" if random.random() < 0.3 else None
            created_at = start_date + timedelta(seconds=random.randint(0, 365 * 24 * 60 * 60))

            reviews.append((product_id, member_id, score, content, image_url, created_at))

        # Insert reviews in batches
        batch_size = 1000
        for i in range(0, len(reviews), batch_size):
            batch = reviews[i:i+batch_size]
            cursor.executemany(review_insert_query, batch)
            connection.commit()
            print(f"Inserted {i+len(batch)} reviews")

        # Update product review counts and average scores
        cursor.execute("""
        UPDATE `product` p
        JOIN (
            SELECT product_id, COUNT(*) as review_count, AVG(score) as avg_score
            FROM `review`
            GROUP BY product_id
        ) r ON p.id = r.product_id
        SET p.review_count = r.review_count, p.score = r.avg_score
        """)
        connection.commit()

        print("Dummy data insertion and product updates completed successfully.")

    except Error as e:
        print(f"Error while inserting dummy data: {e}")
    finally:
        if connection.is_connected():
            cursor.close()

def main():
    connection = create_connection()
    if connection is not None:
        insert_dummy_data(connection)
        connection.close()
    else:
        print("Failed to create database connection.")

if __name__ == "__main__":
    main()