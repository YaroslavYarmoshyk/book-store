package com.online.bookstore.controller;

import com.online.bookstore.dto.order.OrderItemDto;
import com.online.bookstore.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping(value = "/by-order/{orderId}")
    @Operation(
            summary = "Get order items by order ID",
            description = "Get order items by order ID"
    )
    public List<OrderItemDto> getOrderItems(final @PathVariable Long orderId) {
        return orderItemService.getItemsByOrderId(orderId);
    }

    @GetMapping(value = "/{itemId}")
    @Operation(
            summary = "Get order item by ID",
            description = "Get order item ID"
    )
    public OrderItemDto getOrderItem(final @PathVariable Long itemId) {
        return orderItemService.getItemById(itemId);
    }
}
