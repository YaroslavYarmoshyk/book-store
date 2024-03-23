package com.online.bookstore.mapper;

import com.online.bookstore.config.mapper.MapperConfig;
import com.online.bookstore.dto.cart.CartItemDto;
import com.online.bookstore.dto.order.OrderItemDto;
import com.online.bookstore.model.OrderItem;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(final OrderItem orderItem);

    default List<OrderItemDto> toDto(final Set<OrderItem> orderItems) {
        return orderItems.stream()
                .sorted(Comparator.comparing(OrderItem::getId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Mapping(source = "cartItemDto.bookId", target = "book", qualifiedByName = "bookFromId")
    @Mapping(source = "cartItemDto.quantity", target = "quantity")
    @Mapping(source = "bookPrice", target = "price")
    @Mapping(source = "cartItemDto.id", target = "id", ignore = true)
    OrderItem toModel(final CartItemDto cartItemDto, final BigDecimal bookPrice);
}
