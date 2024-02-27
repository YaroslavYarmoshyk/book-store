package com.online.bookstore.service;

import com.online.bookstore.model.Book;
import java.util.List;

public interface BookService {

    Book save(final Book book);

    List<Book> findAll();
}
