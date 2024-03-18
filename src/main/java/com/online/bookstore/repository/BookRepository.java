package com.online.bookstore.repository;

import com.online.bookstore.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(name = "findAllByCategoryId")
    List<Book> findAllByCategoryId(final Long categoryId);

}
