# Book Store Spring Boot Application

## Application Overview

The Book Store Spring Boot Application is designed to facilitate user registration, book browsing, management functionalities, and now includes order processing features. It incorporates Spring Security for authentication and authorization, ensuring secure access to various endpoints based on user roles.

## Technologies and Tools Used

This project is built with a variety of technologies and tools to provide a robust and scalable online bookstore. Here are some of the key technologies used:

- **Java**: The main programming language used for developing the application.

- **Spring Boot**: This framework is used to create stand-alone, production-grade Spring-based applications with minimal effort. It simplifies the setup of Spring and third-party libraries.

- **Spring Security**: This is used for authentication and access-control features. In this project, it's used to secure the REST APIs.

- **Spring Data JPA**: This is used for easier data access and manipulation by creating repository implementations automatically, at runtime, from a repository interface.

- **Swagger**: This tool is used for designing, building, and documenting RESTful APIs. It provides a user-friendly interface to interact with the API and understand its capabilities.

- **SQL**: The language used for interacting with the database.

- **Maven**: This tool is used for managing project dependencies and building the project.

- **JUnit and Mockito**: These are used for unit testing and mocking dependencies.

- **IntelliJ IDEA**: The integrated development environment (IDE) used for developing this application.

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
  - Send a GET request to `/api/orders/{orderId}/items` to retrieve all order items for a specific order.

- **Order Item Processing (User Use Cases)**: Users can place orders and view items within their orders. They can:
  - Send a GET request to `/api/order-items/{itemId}` to retrieve a specific order item within an order.

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

## Running the Application with Docker Compose
To run the application using Docker Compose, you can use the following command:
```bash
docker compose --profile backend up -d
```
To stop the application and remove the containers, networks, and volumes defined in your `compose.yaml` file, you can use the following command:
```bash
docker compose --profile backend down
```
