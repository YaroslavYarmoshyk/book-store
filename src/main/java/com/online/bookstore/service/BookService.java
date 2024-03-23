package com.online.bookstore.service;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(final CreateBookRequestDto requestDto);

    BookDto updateBook(final Long id, final CreateBookRequestDto requestDto);

    BookDto deleteBook(final Long id);

    Page<BookDto> getAllBooks(final Pageable pageable);

    BookDto getBookById(final Long id);

    List<BookDto> getBooksByIds(final Collection<Long> bookIds);

    Page<BookWithoutCategoriesDto> getAllBooksByCategoryId(final Pageable pageable,
                                                           final Long categoryId);
}
