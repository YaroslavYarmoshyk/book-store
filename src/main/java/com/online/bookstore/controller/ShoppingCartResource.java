package com.online.bookstore.controller;

import com.online.bookstore.annotation.CurrentUserId;
import com.online.bookstore.dto.cart.CreateCartItemRequestDto;
import com.online.bookstore.dto.cart.ShoppingCartDto;
import com.online.bookstore.dto.cart.UpdateCartItemRequestDto;
import com.online.bookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping cart management", description = "Endpoint for managing user carts")
public class ShoppingCartResource {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get user cart", description = "Retrieve user's shopping cart")
    public ShoppingCartDto getUserCart(final @CurrentUserId Long userId) {
        return shoppingCartService.getCartByUserId(userId);
    }

    @PostMapping
    @Operation(summary = "Add book to shopping cart", description = "Add book to shopping cart")
    public ShoppingCartDto addBookToCart(final @CurrentUserId Long userId,
                                         final @Valid @RequestBody CreateCartItemRequestDto dto) {
        return shoppingCartService.addBookToCart(userId, dto);
    }

    @PutMapping(value = "/cart-items/{cart-item-id}")
    @Operation(
            summary = "Update quantity of a book in the shopping cart",
            description = "Update quantity of a book in the shopping cart"
    )
    public ShoppingCartDto updateBookQuantity(final @CurrentUserId Long userId,
                                              final @PathVariable("cart-item-id") Long cartItemId,
                                              @Valid @RequestBody UpdateCartItemRequestDto dto) {
        return shoppingCartService.updateBookQuantity(userId, cartItemId, dto);
    }

    @DeleteMapping(value = "/cart-items/{cart-item-id}")
    @Operation(
            summary = "Remove a book from the shopping cart",
            description = "Remove a book from the shopping cart"
    )
    public ShoppingCartDto removeBookFromCart(final @CurrentUserId Long userId,
                                              final @PathVariable("cart-item-id") Long cartItemId) {
        return shoppingCartService.removeBookFromCart(userId, cartItemId);
    }
}
