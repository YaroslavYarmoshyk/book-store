package com.online.bookstore.service.impl;

import static com.online.bookstore.util.DateTimeConstants.DEFAULT_ZONE_ID;

import com.online.bookstore.dto.book.BookDto;
import com.online.bookstore.dto.cart.CartItemDto;
import com.online.bookstore.dto.cart.ShoppingCartDto;
import com.online.bookstore.dto.order.CreateOrderRequestDto;
import com.online.bookstore.dto.order.OrderDto;
import com.online.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.online.bookstore.exception.EntityNotFoundException;
import com.online.bookstore.exception.SystemException;
import com.online.bookstore.mapper.OrderItemMapper;
import com.online.bookstore.mapper.OrderMapper;
import com.online.bookstore.model.Order;
import com.online.bookstore.model.OrderItem;
import com.online.bookstore.model.User;
import com.online.bookstore.model.enumeration.OrderStatus;
import com.online.bookstore.repository.OrderRepository;
import com.online.bookstore.service.BookService;
import com.online.bookstore.service.OrderService;
import com.online.bookstore.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderDto> getOrdersByUserId(final Long userId) {
        return orderMapper.toDto(orderRepository.findAllByUserId(userId));
    }

    @Override
    @Transactional
    public OrderDto placeOrder(final Long userId,
                               final CreateOrderRequestDto requestDto) {
        final ShoppingCartDto cartByUserId = shoppingCartService.getCartByUserId(userId);
        validateShoppingCart(cartByUserId);

        final Set<OrderItem> orderItems = getOrderItems(cartByUserId.cartItems());
        final Order order = getOrder(userId, requestDto.shippingAddress(), orderItems);
        final Order savedOrder = orderRepository.save(order);

        shoppingCartService.releaseCart(cartByUserId.id());

        return orderMapper.toDto(savedOrder);
    }

    private static Order getOrder(final Long userId,
                                  final String shippingAddress,
                                  final Set<OrderItem> orderItems) {
        final User user = new User().setId(userId);
        return new Order()
                .setUser(user)
                .setStatus(OrderStatus.PENDING)
                .setTotal(calculateOrderTotal(orderItems))
                .setOrderDate(LocalDateTime.now(DEFAULT_ZONE_ID))
                .setShippingAddress(shippingAddress)
                .setOrderItems(orderItems);
    }

    private static void validateShoppingCart(ShoppingCartDto cartByUserId) {
        if (cartByUserId.cartItems().isEmpty()) {
            throw new SystemException("Cannot place an order, shopping cart is empty",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private Set<OrderItem> getOrderItems(final List<CartItemDto> cartItems) {
        final Map<Long, BigDecimal> bookPrices = getBookPrices(cartItems);
        return cartItems.stream()
                .map(cartItem -> orderItemMapper.toModel(
                        cartItem,
                        bookPrices.get(cartItem.bookId())
                ))
                .collect(Collectors.toSet());
    }

    private Map<Long, BigDecimal> getBookPrices(final List<CartItemDto> cartItems) {
        final List<Long> bookIds = cartItems.stream()
                .map(CartItemDto::bookId)
                .toList();
        return bookService.getBooksByIds(bookIds).stream()
                .collect(Collectors.toMap(
                        BookDto::id,
                        BookDto::price
                ));
    }

    private static BigDecimal calculateOrderTotal(final Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto updateOrderStatus(final Long orderId,
                                      final UpdateOrderStatusRequestDto requestDto) {
        final Order orderById = getOrderById(orderId);
        orderById.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(orderById));
    }

    private Order getOrderById(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find order by ID: " + orderId
                ));
    }
}
