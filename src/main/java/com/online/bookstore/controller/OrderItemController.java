package com.online.bookstore.controller;

import com.online.bookstore.dto.order.OrderItemDto;
import com.online.bookstore.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/order-items")
@RequiredArgsConstructor
@Tag(name = "Order items management", description = "Endpoint for managing order items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping(value = "/{itemId}")
    @Operation(
            summary = "Get order item by ID",
            description = "Get order item ID"
    )
    public OrderItemDto getOrderItem(final @PathVariable Long itemId) {
        return orderItemService.getItemById(itemId);
    }
}
