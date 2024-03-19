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
