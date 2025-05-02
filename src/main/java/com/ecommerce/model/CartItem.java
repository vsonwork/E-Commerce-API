package com.ecommerce.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class CartItem extends PanacheEntity {
    @ManyToOne
    public User user;

    @ManyToOne
    public Product product;

    public int quantity;
}