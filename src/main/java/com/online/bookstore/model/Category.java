package com.online.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET is_deleted = true WHERE id=?")
@SQLRestriction(value = "is_deleted <> 'true'")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    private String description;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
