package com.online.bookstore.utils;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.book.CreateBookRequestDto;
import com.online.bookstore.dto.category.CategoryDto;
import com.online.bookstore.dto.category.CreateCategoryRequestDto;

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
            "9781402894626",
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

    public static CategoryDto FICTION_CATEGORY = new CategoryDto(
            1L,
            "Fiction",
            "Works of imaginative narration, often including elements not entirely based on reality."
    );

    public static CreateCategoryRequestDto CREATE_HISTORICAL_FICTION_CATEGORY = new CreateCategoryRequestDto(
            "Historical Fiction",
            "Works of fiction set in the past, often incorporating real historical events, people, or settings into the narrative."
    );

    public static final CreateCategoryRequestDto INVALID_NULLABLE_CREATE_CATEGORY_REQUEST = new CreateCategoryRequestDto(
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

    public static CreateCategoryRequestDto getInvalidCreateCategoryRequest() {
        final String name = String.join("", Collections.nCopies(4, "This title is more than fifty characters long, which violates the size constraint."));
        final String description = String.join("", Collections.nCopies(1000, "This description is more than ten thousand characters long, which violates the size constraint."));
        return new CreateCategoryRequestDto(name, description);
    }
}
