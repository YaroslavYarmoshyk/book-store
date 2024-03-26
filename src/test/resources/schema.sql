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

INSERT INTO users (email, password, first_name, last_name, shipping_address)
VALUES ('bob@test.com', '$2a$12$UdMD/WnTmdyBQX4FY4BS6O8GNLRR5CGzwtC6exVuKpULQ6vmhVZZW', 'Bob', 'Odinson', 'Asgard'),
       ('alice@test.com', '$2a$12$n1oNmtcIgwSUQQ4FrItHLuA1jaX1cSg9qBnIxIQ32Ds397J7ByT9.', 'Alice', 'Poppins',
        'Hogwarts'),
       ('john@test.com', '$2a$12$gagao3NtNtsgRPBlAPra5e/aJmzjov9h0v3Yh/ZOe9i5wP.j0c5H2', 'John', 'Wick', 'Lviv');

INSERT INTO roles (name)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'bob@test.com'), (SELECT id FROM roles WHERE name = 'USER')),
       ((SELECT id FROM users WHERE email = 'alice@test.com'), (SELECT id FROM roles WHERE name = 'ADMIN')),
       ((SELECT id FROM users WHERE email = 'john@test.com'), (SELECT id FROM roles WHERE name = 'USER')),
       ((SELECT id FROM users WHERE email = 'john@test.com'), (SELECT id FROM roles WHERE name = 'ADMIN'));

INSERT INTO books (title, author, isbn, price, description, cover_image)
VALUES ('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 10.99,
        'A gripping portrayal of racial injustice and moral growth.', 'https://example.com/to_kill_a_mockingbird.jpg'),
       ('1984', 'George Orwell', '9780451524935', 9.99, 'A dystopian novel set in a totalitarian regime.',
        'https://example.com/1984.jpg'),
       ('Pride and Prejudice', 'Jane Austen', '9780486284736', 7.99,
        'A classic romance novel featuring Elizabeth Bennet and Mr. Darcy.',
        'https://example.com/pride_and_prejudice.jpg'),
       ('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 12.50, 'A tragic love story set in the Jazz Age.',
        'https://example.com/thegreatgatsby.jpg'),
       ('The Catcher in the Rye', 'J.D. Salinger', '9780316769488', 11.25,
        'A coming-of-age novel narrated by a teenager named Holden Caulfield.',
        'https://example.com/thecatcherintherye.jpg'),
       ('To the Lighthouse', 'Virginia Woolf', '9780156907392', 14.99,
        'A novel that explores the passage of time and the nature of human experience.',
        'https://example.com/tothelighthouse.jpg'),
       ('Moby-Dick', 'Herman Melville', '9780142000083', 13.25,
        'An epic tale of one man''s obsession with revenge against a whale.', 'https://example.com/mobydick.jpg'),
       ('Brave New World', 'Aldous Huxley', '9780060850524', 10.75,
        'A dystopian novel depicting a future society controlled by technology.',
        'https://example.com/bravenewworld.jpg'),
       ('The Hobbit', 'J.R.R. Tolkien', '9780345339683', 15.50,
        'A fantasy novel about the quest of the hobbit Bilbo Baggins.', 'https://example.com/thehobbit.jpg'),
       ('Crime and Punishment', 'Fyodor Dostoevsky', '9780679734505', 11.99,
        'A psychological thriller exploring the moral dilemmas of a young student.',
        'https://example.com/crimeandpunishment.jpg'),
       ('The Picture of Dorian Gray', 'Oscar Wilde', '9780486278070', 9.25,
        'A Gothic novel about a young man who remains youthful while his portrait ages.',
        'https://example.com/doriangray.jpg'),
       ('The Lord of the Rings', 'J.R.R. Tolkien', '9780618640157', 25.99,
        'An epic fantasy trilogy about the quest to destroy a powerful ring.',
        'https://example.com/thelordoftherings.jpg'),
       ('Wuthering Heights', 'Emily Brontë', '9780141439556', 8.50, 'A dark romance set on the Yorkshire moors.',
        'https://example.com/wutheringheights.jpg'),
       ('Frankenstein', 'Mary Shelley', '9780486282114', 7.99,
        'A Gothic novel exploring themes of creation and identity.', 'https://example.com/frankenstein.jpg'),
       ('The Adventures of Sherlock Holmes', 'Arthur Conan Doyle', '9780140439083', 10.25,
        'A collection of detective stories featuring the famous detective Sherlock Holmes.',
        'https://example.com/sherlockholmes.jpg');

INSERT INTO categories (name, description)
VALUES
    ('Fiction', 'Works of imaginative narration, often including elements not entirely based on reality.'),
    ('Classic', 'Works of literature that are considered to be of high quality, enduring significance, and timeless appeal.'),
    ('Dystopian', 'Works depicting an imaginary community or society that is undesirable or frightening.'),
    ('Mystery', 'Works involving a mysterious event or crime that needs to be solved.'),
    ('Fantasy', 'Works set in imaginary worlds or featuring magical or supernatural elements.'),
    ('Romance', 'Works focusing on romantic love relationships.'),
    ('Gothic', 'Works characterized by a bleak, mysterious, or eerie atmosphere, often with supernatural or horror elements.'),
    ('Thriller', 'Works intended to create intense excitement, suspense, and anticipation.'),
    ('Adventure', 'Works involving exciting, unusual, or dangerous experiences.'),
    ('Philosophical', 'Works exploring fundamental questions about existence, knowledge, values, reason, mind, and language.');

INSERT INTO books_categories (book_id, category_id)
VALUES
    -- To Kill a Mockingbird
    (1, 1), (1, 2),
    -- 1984
    (2, 1), (2, 3),
    -- Pride and Prejudice
    (3, 1), (3, 6),
    -- The Great Gatsby
    (4, 1), (4, 7),
    -- The Catcher in the Rye
    (5, 1), (5, 9),
    -- To the Lighthouse
    (6, 1), (6, 9),
    -- Moby-Dick
    (7, 1), (7, 9),
    -- Brave New World
    (8, 1), (8, 3),
    -- The Hobbit
    (9, 1), (9, 5),
    -- Crime and Punishment
    (10, 1), (10, 10),
    -- The Picture of Dorian Gray
    (11, 1), (11, 7),
    -- The Lord of the Rings
    (12, 1), (12, 5),
    -- Wuthering Heights
    (13, 1), (13, 6),
    -- Frankenstein
    (14, 1), (14, 7),
    -- The Adventures of Sherlock Holmes
    (15, 1), (15, 4);


