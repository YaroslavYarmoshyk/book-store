# Book store spring boot application
## Application Overview
The application is designed to facilitate user registration, book browsing, and management functionalities. It incorporates Spring Security for authentication and authorization, ensuring secure access to various endpoints based on user roles.

### Key Features
- User Registration
Users can register for an account by providing necessary details via a POST request to /api/auth/register. This functionality is available for non-authenticated users.

- Book Browsing
Authenticated users, both regular users and admins, can browse the book catalog by sending a GET request to /api/books. Additionally, they can view detailed information about a specific book by sending a GET request to /api/books/{id}.

- Book Management (Admin Only)
Admins have additional privileges for managing the book catalog. They can perform the following actions:

  - Add Book: Admins can add a new book to the catalog by sending a POST request to /api/books/.
  - Update Book: Existing book details can be updated by sending a PUT request to /api/books/{id}.
  - Remove Book: Admins can remove a book from the catalog by sending a DELETE request to /api/books/{id}

### Test users
- User:
    - email: bob@test.com
    - password: bobpass123
- Admin:
    - email: alice@test.com
    - password: alicepass123
- Admin/User:
    - email: john@test.com
    - password: johnpass123
