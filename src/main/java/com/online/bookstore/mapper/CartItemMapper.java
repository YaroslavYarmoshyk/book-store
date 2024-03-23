package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.cart.CartItemDto;
import com.online.bookstore.dto.cart.CreateCartItemRequestDto;
import com.online.bookstore.model.CartItem;
import com.online.bookstore.model.ShoppingCart;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(final CartItem cartItem);

    default List<CartItemDto> toDto(final Set<CartItem> cartItems) {
        return cartItems.stream()
                .sorted(Comparator.comparing(CartItem::getId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Mapping(target = "book", source = "requestDto.bookId", qualifiedByName = "bookFromId")
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toModel(final CreateCartItemRequestDto requestDto, final Long shoppingCartId);

    @AfterMapping
    default void setShoppingCart(@MappingTarget CartItem cartItem, Long shoppingCartId) {
        cartItem.setShoppingCart(new ShoppingCart().setId(shoppingCartId));
    }
}
