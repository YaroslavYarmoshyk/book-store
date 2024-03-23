package com.online.bookstore.service;

import com.online.bookstore.dto.order.CreateOrderRequestDto;
import com.online.bookstore.dto.order.OrderDto;
import com.online.bookstore.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(final Long userId);

    OrderDto placeOrder(final Long userId, final CreateOrderRequestDto requestDto);

    OrderDto updateOrderStatus(final Long orderId, final UpdateOrderStatusRequestDto requestDto);
}
