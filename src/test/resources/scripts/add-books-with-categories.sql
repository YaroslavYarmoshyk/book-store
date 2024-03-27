INSERT INTO books (id, title, author, isbn, price, description, cover_image)
VALUES (1, 'To Kill a Mockingbird', 'Harper Lee', '9780061120084', 10.99,
        'A gripping portrayal of racial injustice and moral growth.', 'https://example.com/to_kill_a_mockingbird.jpg'),
       (2, '1984', 'George Orwell', '9780451524935', 9.99, 'A dystopian novel set in a totalitarian regime.',
        'https://example.com/1984.jpg'),
       (3, 'Pride and Prejudice', 'Jane Austen', '9780486284736', 7.99,
        'A classic romance novel featuring Elizabeth Bennet and Mr. Darcy.',
        'https://example.com/pride_and_prejudice.jpg'),
       (4, 'The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 12.50, 'A tragic love story set in the Jazz Age.',
        'https://example.com/thegreatgatsby.jpg'),
       (5, 'The Catcher in the Rye', 'J.D. Salinger', '9780316769488', 11.25,
        'A coming-of-age novel narrated by a teenager named Holden Caulfield.',
        'https://example.com/thecatcherintherye.jpg'),
       (6, 'To the Lighthouse', 'Virginia Woolf', '9780156907392', 14.99,
        'A novel that explores the passage of time and the nature of human experience.',
        'https://example.com/tothelighthouse.jpg'),
       (7, 'Moby-Dick', 'Herman Melville', '9780142000083', 13.25,
        'An epic tale of one man''s obsession with revenge against a whale.', 'https://example.com/mobydick.jpg'),
       (8, 'Brave New World', 'Aldous Huxley', '9780060850524', 10.75,
        'A dystopian novel depicting a future society controlled by technology.',
        'https://example.com/bravenewworld.jpg'),
       (9, 'The Hobbit', 'J.R.R. Tolkien', '9780345339683', 15.50,
        'A fantasy novel about the quest of the hobbit Bilbo Baggins.', 'https://example.com/thehobbit.jpg'),
       (10, 'Crime and Punishment', 'Fyodor Dostoevsky', '9780679734505', 11.99,
        'A psychological thriller exploring the moral dilemmas of a young student.',
        'https://example.com/crimeandpunishment.jpg'),
       (11, 'The Picture of Dorian Gray', 'Oscar Wilde', '9780486278070', 9.25,
        'A Gothic novel about a young man who remains youthful while his portrait ages.',
        'https://example.com/doriangray.jpg'),
       (12, 'The Lord of the Rings', 'J.R.R. Tolkien', '9780618640157', 25.99,
        'An epic fantasy trilogy about the quest to destroy a powerful ring.',
        'https://example.com/thelordoftherings.jpg'),
       (13, 'Wuthering Heights', 'Emily Brontë', '9780141439556', 8.50, 'A dark romance set on the Yorkshire moors.',
        'https://example.com/wutheringheights.jpg'),
       (14, 'Frankenstein', 'Mary Shelley', '9780486282114', 7.99,
        'A Gothic novel exploring themes of creation and identity.', 'https://example.com/frankenstein.jpg'),
       (15, 'The Adventures of Sherlock Holmes', 'Arthur Conan Doyle', '9780140439083', 10.25,
        'A collection of detective stories featuring the famous detective Sherlock Holmes.',
        'https://example.com/sherlockholmes.jpg');

INSERT INTO categories (id, name, description)
VALUES
    (1,'Fiction', 'Works of imaginative narration, often including elements not entirely based on reality.'),
    (2,'Classic', 'Works of literature that are considered to be of high quality, enduring significance, and timeless appeal.'),
    (3,'Dystopian', 'Works depicting an imaginary community or society that is undesirable or frightening.'),
    (4,'Mystery', 'Works involving a mysterious event or crime that needs to be solved.'),
    (5,'Fantasy', 'Works set in imaginary worlds or featuring magical or supernatural elements.'),
    (6,'Romance', 'Works focusing on romantic love relationships.'),
    (7,'Gothic', 'Works characterized by a bleak, mysterious, or eerie atmosphere, often with supernatural or horror elements.'),
    (8,'Thriller', 'Works intended to create intense excitement, suspense, and anticipation.'),
    (9,'Adventure', 'Works involving exciting, unusual, or dangerous experiences.'),
    (10,'Philosophical', 'Works exploring fundamental questions about existence, knowledge, values, reason, mind, and language.');

-- Update sequences
SELECT setval('books_id_seq', (SELECT MAX(id) FROM books));
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));

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
