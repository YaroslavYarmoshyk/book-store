package com.online.bookstore.repository;

import com.online.bookstore.model.Book;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId",
            countQuery = "SELECT COUNT(b) FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    Page<Book> findAllByCategoryId(final Pageable pageable, final Long categoryId);

    @NotNull
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(@NotNull final Long id);

    @NotNull
    @EntityGraph(attributePaths = "categories")
    List<Book> findAllByIdIn(final Collection<Long> bookIds);
}
