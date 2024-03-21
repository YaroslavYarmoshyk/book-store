CREATE TABLE IF NOT EXISTS shopping_carts
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS cart_items
(
    id  BIGSERIAL PRIMARY KEY,
    shopping_cart_id BIGINT REFERENCES shopping_carts(id),
    book_id BIGINT REFERENCES books(id),
    quantity INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
)
