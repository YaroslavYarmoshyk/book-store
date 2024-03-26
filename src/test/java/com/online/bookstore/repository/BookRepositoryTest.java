package com.online.bookstore.repository;

import com.online.bookstore.model.Book;
import com.online.bookstore.model.Category;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = "/scripts/add-books-with-categories.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = "/scripts/remove-books-with-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @ParameterizedTest
    @MethodSource(value = "findByCategoryArgumentsProvider")
    @DisplayName("Test find books by category ID in page representation")
    void findAllByCategoryId_WhenExists_ShouldReturnPageWithBooks(final Pageable pageable,
                                                                  final long categoryId,
                                                                  final long expectedNumOfElements,
                                                                  final long expectedTotalBooks) {
        final Page<Book> actual = bookRepository.findAllByCategoryId(pageable, categoryId);

        assertThat(actual.getPageable().getPageNumber()).isEqualTo(pageable.getPageNumber());
        assertThat(actual.getPageable().getPageSize()).isEqualTo(pageable.getPageSize());
        assertThat(actual.getNumberOfElements()).isEqualTo(expectedNumOfElements);
        assertThat(actual.getTotalElements()).isEqualTo(expectedTotalBooks);
        assertThat(actual.getContent())
                .extracting(Book::getCategories)
                .allSatisfy(categories -> assertThat(categories)
                        .extracting(Category::getId)
                        .contains(categoryId)
                );
    }

    @Test
    @DisplayName("Test categories are fetched with find by book ID")
    void findById_WhenBookExists_ThenCategoriesAreFetched() {
        final Optional<Book> bookById = bookRepository.findById(1L);

        assertThat(bookById)
                .isPresent()
                .hasValueSatisfying(book -> assertThat(book.getCategories()).isNotEmpty());
    }

    @Test
    @DisplayName("Test categories are fetched with find books by IDs")
    void findByIdIn_WhenBooksExist_ThenCategoriesAreFetched() {
        final List<Book> booksByIds = bookRepository.findAllByIdIn(List.of(1L, 2L, 3L));

        final SoftAssertions softy = new SoftAssertions();
        softy.assertThat(booksByIds).isNotEmpty();
        softy.assertThat(booksByIds)
                .extracting(Book::getCategories)
                .isNotEmpty();
        softy.assertAll();
    }

    private static Stream<Arguments> findByCategoryArgumentsProvider() {
        return Stream.of(
                Arguments.of(PageRequest.of(0, 5), 1, 5, 15),
                Arguments.of(PageRequest.of(0, 6), 1, 6, 15),
                Arguments.of(PageRequest.of(1, 5), 1, 5, 15),
                Arguments.of(PageRequest.of(0, 5), 2, 1, 1),
                Arguments.of(PageRequest.of(1, 2), 7, 1, 3)
        );
    }
}
