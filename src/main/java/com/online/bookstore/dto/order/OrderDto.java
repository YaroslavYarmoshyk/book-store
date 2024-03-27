package com.online.bookstore.dto.order;

import static com.online.bookstore.constants.DateTimeConstants.DEFAULT_DATE_TIME_FORMAT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.online.bookstore.model.enumeration.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        Long userId,
        @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMAT) LocalDateTime orderDate,
        OrderStatus status,
        BigDecimal total,
        List<OrderItemDto> orderItems
) {
}
