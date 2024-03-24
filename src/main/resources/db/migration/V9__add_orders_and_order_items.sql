CREATE TABLE IF NOT EXISTS orders
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    status TEXT NOT NULL CHECK (status = 'PENDING' OR status = 'DELIVERED' OR status = 'COMPLETED'),
    total DECIMAL(19,2) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    shipping_address TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS order_items
(
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    book_id BIGINT REFERENCES books(id),
    quantity INT NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
)
