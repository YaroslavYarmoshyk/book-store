package com.online.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = {"shoppingCart", "book"})
@EqualsAndHashCode(exclude = {"shoppingCart", "book"}, callSuper = true)
@Entity
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted <> 'true'")
public class CartItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(nullable = false)
    private int quantity;
}
