package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.cart.CartItemDto;
import com.online.bookstore.dto.order.OrderItemDto;
import com.online.bookstore.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(final OrderItem orderItem);

    default List<OrderItemDto> toDto(final List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Mapping(source = "cartItemDto.bookId", target = "book", qualifiedByName = "bookFromId")
    @Mapping(source = "bookPrice", target = "price")
    @Mapping(target = "id", ignore = true)
    OrderItem toModel(final CartItemDto cartItemDto, final BigDecimal bookPrice);
}
