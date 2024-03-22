package com.online.bookstore.service.impl;

import com.online.bookstore.dto.cart.CreateCartItemRequestDto;
import com.online.bookstore.dto.cart.ShoppingCartDto;
import com.online.bookstore.dto.cart.UpdateCartItemRequestDto;
import com.online.bookstore.exception.SystemException;
import com.online.bookstore.mapper.CartItemMapper;
import com.online.bookstore.mapper.ShoppingCartMapper;
import com.online.bookstore.model.CartItem;
import com.online.bookstore.model.ShoppingCart;
import com.online.bookstore.model.User;
import com.online.bookstore.repository.ShoppingCartRepository;
import com.online.bookstore.service.ShoppingCartService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getCartByUserId(final Long userId) {
        return shoppingCartMapper.toDto(getCartEntityByUserId(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToCart(final Long userId,
                                         final CreateCartItemRequestDto requestDto) {
        final ShoppingCart cartByUserId = getCartEntityByUserId(userId);
        final CartItem cartItem = cartByUserId.getCartItems().stream()
                .filter(item -> Objects.equals(item.getBook().getId(), requestDto.bookId()))
                .peek(item -> item.setQuantity(requestDto.quantity()))
                .findFirst()
                .orElseGet(() -> cartItemMapper.toModel(requestDto, cartByUserId.getId()));

        cartByUserId.getCartItems().add(cartItem);
        final ShoppingCart savedCart = shoppingCartRepository.save(cartByUserId);

        return shoppingCartMapper.toDto(savedCart);
    }

    @Override
    public ShoppingCartDto updateBookQuantity(final Long userId,
                                              final Long cartItemId,
                                              final UpdateCartItemRequestDto requestDto) {
        final ShoppingCart cartByUserId = getCartEntityByUserId(userId);
        final CartItem cartItem = cartByUserId.getCartItems().stream()
                .filter(item -> Objects.equals(item.getId(), cartItemId))
                .findFirst()
                .orElseThrow(() -> new SystemException(
                        "Required item don't belong to user", HttpStatus.FORBIDDEN
                ));

        cartItem.setQuantity(requestDto.quantity());
        final ShoppingCart updatedCart = shoppingCartRepository.save(cartByUserId);

        return shoppingCartMapper.toDto(updatedCart);
    }

    @Override
    public ShoppingCartDto removeBookFromCart(final Long userId, final Long cartItemId) {
        final ShoppingCart cartByUserId = getCartEntityByUserId(userId);

        cartByUserId.getCartItems().removeIf(item -> Objects.equals(item.getId(), cartItemId));
        final ShoppingCart updatedCart = shoppingCartRepository.save(cartByUserId);

        return shoppingCartMapper.toDto(updatedCart);
    }

    private ShoppingCart getCartEntityByUserId(final Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createNewShoppingCart(userId));
    }

    private ShoppingCart createNewShoppingCart(final Long userId) {
        final User user = new User().setId(userId);
        return shoppingCartRepository.save(new ShoppingCart().setUser(user));
    }
}
