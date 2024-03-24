package com.online.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrderRequestDto(@NotNull @Size(max = 100) String shippingAddress) {
}
