package com.online.bookstore.service;

import com.online.bookstore.dto.order.OrderItemDto;
import java.util.List;

public interface OrderItemService {
    OrderItemDto getItemById(final Long itemId);

    List<OrderItemDto> getItemsByOrderId(final Long orderId);
}
