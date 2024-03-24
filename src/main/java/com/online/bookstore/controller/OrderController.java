package com.online.bookstore.controller;

import com.online.bookstore.annotation.AdminAccessLevel;
import com.online.bookstore.annotation.CurrentUserId;
import com.online.bookstore.dto.order.CreateOrderRequestDto;
import com.online.bookstore.dto.order.OrderDto;
import com.online.bookstore.dto.order.OrderItemDto;
import com.online.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.online.bookstore.service.OrderItemService;
import com.online.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order management", description = "Endpoint for managing orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping
    @Operation(summary = "Get user orders", description = "Get user orders")
    public List<OrderDto> getOrdersByUserId(final @CurrentUserId Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PostMapping
    @Operation(summary = "Place an order", description = "Place and order")
    public OrderDto placeOrder(final @CurrentUserId Long userId,
                               final @Valid @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.placeOrder(userId, requestDto);
    }

    @AdminAccessLevel
    @PatchMapping(value = "/{id}")
    @Operation(
            summary = "Update order status by order ID",
            description = "Update order status by order ID"
    )
    public OrderDto updateOrderStatus(final @PathVariable Long id,
                                      final @RequestBody UpdateOrderStatusRequestDto status) {
        return orderService.updateOrderStatus(id, status);
    }

    @GetMapping(value = "/{id}/items")
    @Operation(
            summary = "Get order items by order ID",
            description = "Get order items by order ID"
    )
    public List<OrderItemDto> getOrderItems(final @PathVariable Long id) {
        return orderItemService.getItemsByOrderId(id);
    }
}
