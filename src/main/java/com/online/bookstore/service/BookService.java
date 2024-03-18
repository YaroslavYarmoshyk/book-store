package com.online.bookstore.service;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto createBook(final CreateBookRequestDto requestDto);

    BookDto updateBook(final Long id, final CreateBookRequestDto requestDto);

    BookDto deleteBook(final Long id);

    List<BookDto> getAllBooks(final Pageable pageable);

    BookDto getBookById(final Long id);
}
