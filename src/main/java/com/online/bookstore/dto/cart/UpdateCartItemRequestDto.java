package com.online.bookstore.dto.cart;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequestDto(@Min(1) Integer quantity) {
}
