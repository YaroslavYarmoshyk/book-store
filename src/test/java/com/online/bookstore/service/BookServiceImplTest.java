package com.online.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.BookWithoutCategoriesDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.mapper.BookMapper;
import com.online.bookstore.model.Book;
import com.online.bookstore.repository.BookRepository;
import com.online.bookstore.service.impl.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(value = MockitoExtension.class)
class BookServiceImplTest {
    public static final Long EXISTING_ID = 1L;
    public static final Long NON_EXISTING_ID = 100L;
    @Spy
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Test book creation")
    void createBook_SaveEntity_Success() {
        final CreateBookRequestDto createBookRequest = mock(CreateBookRequestDto.class);
        final Book book = mock(Book.class);
        final BookDto bookDto = mock(BookDto.class);
        when(bookMapper.toModel(createBookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        bookService.createBook(createBookRequest);

        verify(bookMapper).toModel(createBookRequest);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Test update book by non-existing ID")
    void updateBook_WhenNonExistingId_ThrowException() {
        final CreateBookRequestDto createBookRequest = mock(CreateBookRequestDto.class);
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> bookService.updateBook(NON_EXISTING_ID, createBookRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book by id: " + NON_EXISTING_ID + " was not found");
    }

    @Test
    @DisplayName("Test update book by existing ID")
    void updateBook_ValidId_Success() {
        final CreateBookRequestDto createBookRequest = mock(CreateBookRequestDto.class);
        final Book book = mock(Book.class);
        final BookDto bookDto = mock(BookDto.class);
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(bookMapper.toModel(createBookRequest)).thenReturn(book);
        when(bookRepository.save(book.setId(EXISTING_ID))).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        bookService.updateBook(EXISTING_ID, createBookRequest);

        verify(bookMapper).toModel(createBookRequest);
        verify(bookRepository).save(book.setId(EXISTING_ID));
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Test delete book by non-existing ID")
    void deleteBook_WhenNonExistingId_ThrowException() {
        doThrow(new EntityNotFoundException("Book by id: " + NON_EXISTING_ID + " was not found"))
                .when(bookService)
                .deleteBook(NON_EXISTING_ID);

        assertThatThrownBy(() -> bookService.deleteBook(NON_EXISTING_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book by id: " + NON_EXISTING_ID + " was not found");
    }

    @Test
    @DisplayName("Test delete book by ID")
    void deleteBook_ByExistingId_Success() {
        final BookDto bookDto = Mockito.mock(BookDto.class);
        when(bookDto.id()).thenReturn(EXISTING_ID);
        doReturn(bookDto).when(bookService).getBookById(EXISTING_ID);

        final BookDto actual = bookService.deleteBook(EXISTING_ID);

        assertThat(actual).isEqualTo(bookDto);
        verify(bookRepository).deleteById(EXISTING_ID);
    }

    @Test
    @DisplayName("Test get all books pageable")
    void getAllBooks_ReturnsPageOfBooks() {
        final Pageable pageable = PageRequest.of(0, 5);
        final Book firstBook = mock(Book.class);
        final Book secondBook = mock(Book.class);
        final BookDto firstBookDto = mock(BookDto.class);
        final BookDto secondBookDto = mock(BookDto.class);
        final Page<Book> bookPage = new PageImpl<>(List.of(firstBook, secondBook), pageable, 2);
        when(firstBook.getId()).thenReturn(1L);
        when(secondBook.getId()).thenReturn(2L);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookService.getBooksByIds(List.of(1L, 2L)))
                .thenReturn(List.of(firstBookDto, secondBookDto));

        final Page<BookDto> actualPage = bookService.getAllBooks(pageable);

        assertThat(actualPage.getTotalElements()).isEqualTo(2);
        assertThat(actualPage.getContent().getFirst()).isEqualTo(firstBookDto);
        verify(bookRepository).findAll(pageable);
        verify(bookService).getBooksByIds(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Test get book by existing ID")
    void getBookById_ExistingId_ReturnsBook() {
        final Book book = Mockito.mock(Book.class);
        final BookDto bookDto = Mockito.mock(BookDto.class);
        when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        final BookDto actual = bookService.getBookById(NON_EXISTING_ID);

        assertThat(actual).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Test get book by non-existing ID")
    void getBookById_NonExistingId_ThrowsException() {
        when(bookRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookService.getBookById(NON_EXISTING_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book by id: " + NON_EXISTING_ID + " was not found");
    }

    @Test
    @DisplayName("Test get books by IDs")
    void getBooksByIds_ReturnsListOfBooks() {
        final List<Long> ids = List.of(1L, 2L);
        final Book book1 = Mockito.mock(Book.class);
        final Book book2 = Mockito.mock(Book.class);
        final BookDto bookDto1 = Mockito.mock(BookDto.class);
        final BookDto bookDto2 = Mockito.mock(BookDto.class);
        when(bookRepository.findAllByIdIn(ids)).thenReturn(List.of(book1, book2));
        when(bookRepository.findAllByIdIn(ids)).thenReturn(List.of(book1, book2));
        when(bookMapper.toDto(List.of(book1, book2))).thenReturn(List.of(bookDto1, bookDto2));

        final List<BookDto> actual = bookService.getBooksByIds(ids);

        assertThat(actual).containsExactlyInAnyOrder(bookDto1, bookDto2);
    }

    @Test
    @DisplayName("Test get all books by category ID")
    void getAllBooksByCategoryId_ReturnsPageOfBookWithoutCategories() {
        final Pageable pageable = PageRequest.of(0, 5);
        final Book book1 = Mockito.mock(Book.class);
        final Book book2 = Mockito.mock(Book.class);
        final BookWithoutCategoriesDto bookDto1 = Mockito.mock(BookWithoutCategoriesDto.class);
        final BookWithoutCategoriesDto bookDto2 = Mockito.mock(BookWithoutCategoriesDto.class);
        final Page<Book> bookPage = new PageImpl<>(List.of(book1, book2), pageable, 2);
        when(bookRepository.findAllByCategoryId(pageable, EXISTING_ID)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookDto1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookDto2);

        final var actual = bookService.getAllBooksByCategoryId(pageable, EXISTING_ID);

        assertThat(actual.getTotalElements()).isEqualTo(2);
        assertThat(actual.getContent()).containsExactlyInAnyOrder(bookDto1, bookDto2);
    }

}
