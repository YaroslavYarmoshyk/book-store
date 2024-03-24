# Book Store Spring Boot Application

## Application Overview

The Book Store Spring Boot Application is designed to facilitate user registration, book browsing, management functionalities, and now includes order processing features. It incorporates Spring Security for authentication and authorization, ensuring secure access to various endpoints based on user roles.

## Key Features

- **User Registration**: Users can register for an account by providing necessary details via a POST request to `/api/auth/register`. This functionality is available for non-authenticated users.

- **Book Browsing**: Authenticated users, both regular users and admins, can browse the book catalog by sending a GET request to `/api/books`. Additionally, they can view detailed information about a specific book by sending a GET request to `/api/books/{id}`.

- **Book Management (Admin Only)**: Admins have additional privileges for managing the book catalog. They can perform the following actions:
  - Add Book: Admins can add a new book to the catalog by sending a POST request to `/api/books/`.
  - Update Book: Existing book details can be updated by sending a PUT request to `/api/books/{id}`.
  - Remove Book: Admins can remove a book from the catalog by sending a DELETE request to `/api/books/{id}`.

- **Category Browsing (User Use Cases)**: Users can browse categories to find books by category. They can:
  - Send a GET request to `/api/categories` to retrieve all categories.
  - Send a GET request to `/api/categories/{id}/books` to retrieve books by a specific category.

- **Category Management (Admin Use Cases)**: Admins can manage categories effectively. They can:
  - Create Category: Admins can create a new category by sending a POST request to `/api/categories` with the details of the new category.
  - Update Category: Admins can update the details of a category by sending a PUT request to `/api/categories/{id}` with the updated details of the category.
  - Remove Category: Admins can remove a category by sending a DELETE request to `/api/categories/{id}`.
  - Retrieve Books by Category: Admins and users can retrieve books by a specific category by sending a GET request to `/api/categories/{id}/books`.

- **Shopping Cart Management (User Use Cases)**: Users can manage their shopping carts, including adding, viewing, updating, and removing items. They can:
  - Send a POST request to `/api/cart` to add a book to the shopping cart.
  - Send a GET request to `/api/cart` to retrieve their shopping cart.
  - Send a PUT request to `/api/cart/cart-items/{cartItemId}` to update the quantity of a book in the shopping cart.
  - Send a DELETE request to `/api/cart/cart-items/{cartItemId}` to remove a book from the shopping cart.

- **Order Processing (User Use Cases)**: Users can place orders and view their order history. They can:
  - Send a POST request to `/api/orders` to place an order.
  - Send a GET request to `/api/orders` to retrieve their order history.

- **Order Item Processing (User Use Cases)**: Users can place orders and view items within their orders. They can:
  - Send a GET request to `/api/order-items/{itemId}` to retrieve a specific order item within an order.
  - Send a GET request to `/api/order-items/by-order/{orderId}` to retrieve all order items for a specific order.

- **Order Management (Admin Use Cases)**: Admins can manage orders by updating order status. They can:
  - Send a PATCH request to `/api/orders/{id}` to update the status of an order.

## Test Users

- **User**:
  - email: bob@test.com
  - password: bobpass123

- **Admin**:
  - email: alice@test.com
  - password: alicepass123

- **Admin/User**:
  - email: john@test.com
  - password: johnpass123
