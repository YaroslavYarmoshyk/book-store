package com.online.bookstore.service.impl;

import com.online.bookstore.dto.cart.CreateCartItemRequestDto;
import com.online.bookstore.dto.cart.ShoppingCartDto;
import com.online.bookstore.dto.cart.UpdateCartItemRequestDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.exception.SystemException;
import com.online.bookstore.mapper.CartItemMapper;
import com.online.bookstore.mapper.ShoppingCartMapper;
import com.online.bookstore.model.CartItem;
import com.online.bookstore.model.ShoppingCart;
import com.online.bookstore.model.User;
import com.online.bookstore.repository.CartItemRepository;
import com.online.bookstore.repository.ShoppingCartRepository;
import com.online.bookstore.service.ShoppingCartService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getCartByUserId(final Long userId) {
        final ShoppingCart shoppingCartByUserId = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createNewShoppingCart(userId));
        return shoppingCartMapper.toDto(shoppingCartByUserId);
    }

    private ShoppingCart createNewShoppingCart(final Long userId) {
        final User user = new User().setId(userId);
        return shoppingCartRepository.save(new ShoppingCart().setUser(user));
    }

    @Override
    public ShoppingCartDto addBookToCart(final Long userId,
                                         final CreateCartItemRequestDto requestDto) {
        final ShoppingCartDto cartByUserId = getCartByUserId(userId);
        final CartItem cartItemDto = getExistingOrCreateNewItem(requestDto, cartByUserId);

        cartItemDto.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItemDto);

        return getCartByUserId(userId);
    }

    private CartItem getExistingOrCreateNewItem(final CreateCartItemRequestDto requestDto,
                                                final ShoppingCartDto cartByUserId) {
        return cartByUserId.cartItems().stream()
                .filter(item -> Objects.equals(item.bookId(), requestDto.bookId()))
                .map(cartItemDto -> cartItemMapper.toModel(cartItemDto, cartByUserId.id()))
                .findFirst()
                .orElseGet(() -> cartItemMapper.toModel(requestDto, cartByUserId.id()));
    }

    @Override
    public ShoppingCartDto updateBookQuantity(final Long userId,
                                              final Long cartItemId,
                                              final UpdateCartItemRequestDto requestDto) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find cart item by id: " + cartItemId
                ));
        if (belongsToUser(userId, cartItem.getId())) {
            cartItem.setQuantity(requestDto.quantity());
            cartItemRepository.save(cartItem);
            return getCartByUserId(userId);
        }
        throw new SystemException("Required item don't belong to user", HttpStatus.FORBIDDEN);
    }

    @Override
    public ShoppingCartDto removeBookFromCart(final Long userId, final Long cartItemId) {
        if (belongsToUser(userId, cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
            return getCartByUserId(userId);
        }
        throw new SystemException("Required item don't belong to user", HttpStatus.FORBIDDEN);
    }

    private boolean belongsToUser(final Long userId, final Long cartItemId) {
        return getCartByUserId(userId).cartItems().stream()
                .anyMatch(item -> Objects.equals(item.id(), cartItemId));
    }
}
