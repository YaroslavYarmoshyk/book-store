package com.online.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCartItemRequestDto(
        @NotNull @Min(1) Long bookId,
        @NotNull @Min(1) Integer quantity
) {
}
