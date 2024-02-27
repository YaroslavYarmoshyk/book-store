package com.online.bookstore;

import com.online.bookstore.model.Book;
import com.online.bookstore.service.BookService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class BookStoreApplication {
    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            generateBooks(3).forEach(bookService::save);
            log.info("There are the following books in the store: {}", bookService.findAll());
        };
    }

    private static List<Book> generateBooks(final int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(BookStoreApplication::getBookByNumber)
                .toList();
    }

    private static Book getBookByNumber(final int number) {
        return new Book().setTitle("Title %d".formatted(number))
                .setDescription("Description %d".formatted(number))
                .setCoverImage("Coverage image %d".formatted(number))
                .setAuthor("Author %d".formatted(number))
                .setIsbn("Isbn %d".formatted(number))
                .setPrice(BigDecimal.valueOf(new Random().nextDouble(1000.0)))
                .setDescription("Description %d".formatted(number))
                .setDescription("Description %d".formatted(number));
    }

}
