package com.online.bookstore.dto.order;

public record OrderItemDto(
        Long id,
        Long bookId,
        Integer quantity
) {
}
