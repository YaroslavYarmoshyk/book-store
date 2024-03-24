package com.online.bookstore.dto.order;

import com.online.bookstore.model.enumeration.OrderStatus;
import lombok.NonNull;

public record UpdateOrderStatusRequestDto(@NonNull OrderStatus status) {
}
