CREATE TABLE IF NOT EXISTS book
(
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    isbn TEXT NOT NULL UNIQUE,
    price DECIMAL NOT NULL,
    description TEXT,
    cover_image TEXT
);
