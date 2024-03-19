package com.online.bookstore.service.impl;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.mapper.BookMapper;
import com.online.bookstore.model.Book;
import com.online.bookstore.repository.BookRepository;
import com.online.bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public BookDto updateBook(final Long id, final CreateBookRequestDto requestDto) {
        if (bookRepository.existsById(id)) {
            final Book book = mapper.toModel(requestDto).setId(id);
            return mapper.toDto(bookRepository.save(book));
        }
        throw new EntityNotFoundException("Book by id: " + id + " was not found");
    }

    @Override
    public BookDto deleteBook(final Long id) {
        final BookDto bookById = getBookById(id);
        bookRepository.deleteById(bookById.id());
        return bookById;
    }

    @Override
    public Page<BookDto> getAllBooks(final Pageable pageable) {
        final Page<Book> bookPage = bookRepository.findAll(pageable);
        final List<Long> bookIds = bookPage.stream()
                .map(Book::getId)
                .toList();
        final List<BookDto> booksByIds = mapper.toDto(bookRepository.findAllByIdIn(bookIds));
        return new PageImpl<>(booksByIds, pageable, bookPage.getTotalElements());
    }

    @Override
    public BookDto getBookById(final Long id) {
        return bookRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book by id: " + id + " was not found")
                );
    }

    @Override
    public Page<BookWithoutCategoriesDto> getAllBooksByCategoryId(final Pageable pageable,
                                                                  final Long categoryId) {
        return bookRepository.findAllByCategoryId(pageable, categoryId)
                .map(mapper::toDtoWithoutCategories);
    }
}
