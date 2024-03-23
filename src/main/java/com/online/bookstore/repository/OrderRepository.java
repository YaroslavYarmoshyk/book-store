package com.online.bookstore.repository;

import com.online.bookstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @NotNull
    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findById(@NotNull final Long orderId);

    @EntityGraph(attributePaths = "orderItems")
    List<Order> findAllByUserId(final Long userId);
}
