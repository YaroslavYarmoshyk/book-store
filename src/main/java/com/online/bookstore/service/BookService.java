package com.online.bookstore.service;

import com.online.bookstore.dto.BookDto;
import com.online.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {

    BookDto createBook(final CreateBookRequestDto requestDto);

    List<BookDto> getAllBooks();

    BookDto getBookById(final Long id);
}
