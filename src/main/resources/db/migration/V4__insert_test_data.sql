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
