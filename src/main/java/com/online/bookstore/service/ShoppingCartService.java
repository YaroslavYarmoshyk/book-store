package com.online.bookstore.service;

import com.online.bookstore.dto.cart.CreateCartItemRequestDto;
import com.online.bookstore.dto.cart.ShoppingCartDto;
import com.online.bookstore.dto.cart.UpdateCartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto getCartByUserId(final Long userId);

    ShoppingCartDto addBookToCart(final Long userId, final CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateBookQuantity(final Long userId,
                                       final Long cartItemId,
                                       final UpdateCartItemRequestDto requestDto);

    ShoppingCartDto removeBookFromCart(final Long userId, final Long cartItemId);

    void releaseCart(final Long shoppingCartId);
}
