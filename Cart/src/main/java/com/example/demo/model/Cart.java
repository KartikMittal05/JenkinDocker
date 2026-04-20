package com.example.demo.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Cart {

    @Id
    private Long cartId;
    @ElementCollection
    private Map<Long, String> products = new HashMap<>();

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Map<Long, String> getProducts() { return products; }
    public void setProducts(Map<Long, String> products) {
        this.products = products;
    }
}
