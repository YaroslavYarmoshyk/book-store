package com.online.bookstore.utils;

import com.online.bookstore.dto.book.BookDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public final class TestDataFactory {
    public static final BookDto THE_GREAT_GATSBY = new BookDto(
            4L,
            "The Great Gatsby",
            "F. Scott Fitzgerald",
            "9780743273565",
            BigDecimal.valueOf(12.50).setScale(2, RoundingMode.HALF_UP),
            "A tragic love story set in the Jazz Age.",
            "https://example.com/thegreatgatsby.jpg",
            Set.of(1L, 7L)
    );
}
