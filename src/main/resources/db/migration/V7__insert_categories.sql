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
