package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.cart.ShoppingCartDto;
import com.online.bookstore.model.CartItem;
import com.online.bookstore.model.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class,
        uses = {UserMapper.class, CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(final ShoppingCart shoppingCart);

    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "cartItems", ignore = true)
    ShoppingCart toModel(final ShoppingCartDto shoppingCartDto);

    @AfterMapping
    default void setCartItems(final @MappingTarget ShoppingCart shoppingCart,
                              final ShoppingCartDto shoppingCartDto) {
        final Set<CartItem> cartItems = shoppingCartDto.cartItems().stream()
                .map(item -> new CartItem().setId(item.id()))
                .collect(Collectors.toSet());
        shoppingCart.setCartItems(cartItems);
    }
}
