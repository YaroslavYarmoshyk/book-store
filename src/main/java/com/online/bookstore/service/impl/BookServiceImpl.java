package com.online.bookstore.service.impl;

import com.online.bookstore.dto.BookDto;
import com.online.bookstore.dto.CreateBookRequestDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.mapper.BookMapper;
import com.online.bookstore.model.Book;
import com.online.bookstore.repository.BookRepository;
import com.online.bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper mapper;

    @Override
    public BookDto createBook(final CreateBookRequestDto requestDto) {
        final Book savedBook = bookRepository.save(mapper.toModel(requestDto));
        return mapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(final Long id) {
        return bookRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

}
