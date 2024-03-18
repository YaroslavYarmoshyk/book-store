package com.online.bookstore.repository;

import com.online.bookstore.model.Book;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(name = "findAllByCategoryId")
    List<Book> findAllByCategoryId(final Long categoryId);

    @NotNull
    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(final @NotNull Pageable pageable);
}
