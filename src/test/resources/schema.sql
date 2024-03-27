CREATE TABLE IF NOT EXISTS books
(
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    isbn TEXT NOT NULL UNIQUE,
    price DECIMAL NOT NULL,
    description TEXT,
    cover_image TEXT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    shipping_address TEXT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS roles
(
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOt EXISTS users_roles
(
    user_id BIGINT REFERENCES users(id),
    role_id BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOt EXISTS books_categories
(
    book_id BIGINT REFERENCES books(id),
    category_id BIGINT REFERENCES categories(id),
    PRIMARY KEY (book_id, category_id)
);

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
);

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
);
