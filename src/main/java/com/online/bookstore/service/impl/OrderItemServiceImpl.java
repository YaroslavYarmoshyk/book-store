package com.online.bookstore.service.impl;

import com.online.bookstore.dto.order.OrderItemDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.mapper.OrderItemMapper;
import com.online.bookstore.repository.OrderItemRepository;
import com.online.bookstore.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemDto getItemById(final Long itemId) {
        return orderItemRepository.findById(itemId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find item by ID: " + itemId
                ));
    }

    @Override
    public List<OrderItemDto> getItemsByOrderId(final Long orderId) {
        return orderItemMapper.toDto(orderItemRepository.findAllByOrderId(orderId));
    }
}
