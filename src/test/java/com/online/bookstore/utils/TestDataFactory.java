package com.online.bookstore.utils;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Set;

public final class TestDataFactory {
    public static final BookDto THE_GREAT_GATSBY_BOOK = new BookDto(
            4L,
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            "9780743273565",
            BigDecimal.valueOf(12.50).setScale(2, RoundingMode.HALF_UP),
            "A tragic love story set in the Jazz Age.",
            "https://example.com/thegreatgatsby.jpg",
            Set.of(1L, 7L)
    );

    public static final CreateBookRequestDto CREATE_THE_GREAT_GATSBY_BOOK = new CreateBookRequestDto(
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            "978-0-545-01022-1",
            BigDecimal.valueOf(12.50).setScale(2, RoundingMode.HALF_UP),
            "A tragic love story set in the Jazz Age.",
            "https://example.com/thegreatgatsby.jpg",
            Set.of(1L, 7L)
    );

    public static final CreateBookRequestDto INVALID_NULLABLE_CREATE_BOOK_REQUEST = new CreateBookRequestDto(
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );

    public static CreateBookRequestDto getInvalidCreateBookRequest() {
        final String title = "This title is more than fifty characters long, which violates the size constraint.";
        final String author = "This author name is more than one hundred characters long, which violates the size constraint. This author name is more than one hundred characters long, which violates the size constraint.";
        final String isbn = "InvalidISBN";
        final BigDecimal price = BigDecimal.valueOf(-1);
        final String description = "This description is more than one thousand characters long, which violates the size constraint. This description is more than one thousand characters long, which violates the size constraint. This description is more than one thousand characters long, which violates the size constraint. This description is more than one thousand characters long, which violates the size constraint. This description is more than one thousand characters long, which violates the size constraint.";
        final String coverImage = "";
        final Set<Long> categoryIds = Collections.emptySet();

        return new CreateBookRequestDto(title, author, isbn, price, description, coverImage, categoryIds);
    }
}
