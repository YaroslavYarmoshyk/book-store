package com.online.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.ISBN;

public record CreateBookRequestDto(
        @NotNull
        @Size(max = 50)
        String title,
        @NotNull
        @Size(max = 100)
        String author,
        @NotNull
        @ISBN
        String isbn,
        @NotNull
        @Min(0)
        BigDecimal price,
        @NotNull
        @Size(max = 1000)
        String description,
        String coverImage
) {
}
