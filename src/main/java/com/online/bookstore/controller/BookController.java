package com.online.bookstore.controller;

import com.online.bookstore.annotation.AdminAccessLevel;
import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/books")
@RequiredArgsConstructor
@Tag(name = "Book management", description = "Endpoint for managing books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get all books by criteria")
    public Page<BookDto> getAllBooks(final Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get book by ID", description = "Get specific book")
    public BookDto getBookById(final @PathVariable(value = "id") Long id) {
        return bookService.getBookById(id);
    }

    @AdminAccessLevel
    @PostMapping
    @Operation(summary = "Create a new book", description = "Create a new book")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDto createBook(final @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @AdminAccessLevel
    @PutMapping(value = "/{id}")
    @Operation(summary = "Update book by ID", description = "Update specific book")
    public BookDto updateBook(final @PathVariable(value = "id") Long id,
                              final @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.updateBook(id, requestDto);
    }

    @AdminAccessLevel
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete book by ID", description = "Delete specific book")
    public BookDto deleteBook(final @PathVariable(value = "id") Long id) {
        return bookService.deleteBook(id);
    }
}
