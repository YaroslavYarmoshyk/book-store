package com.online.bookstore.service;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(final CreateBookRequestDto requestDto);

    BookDto updateBook(final Long id, final CreateBookRequestDto requestDto);

    BookDto deleteBook(final Long id);

    Page<BookDto> getAllBooks(final Pageable pageable);

    BookDto getBookById(final Long id);

    Page<BookWithoutCategoriesDto> getAllBooksByCategoryId(final Pageable pageable,
                                                           final Long categoryId);
}
