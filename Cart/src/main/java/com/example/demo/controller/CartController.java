package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Cart;
import com.example.demo.model.ProductCatalog;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService service;

    @PostMapping("/{cartId}/{pid}")
    public Map<String, Object> add(@PathVariable Long cartId, @PathVariable Long pid) {
        return service.add(cartId, pid);
    }

    @GetMapping("/{cartId}")
    public List<ProductCatalog> get(@PathVariable Long cartId) {
        return service.get(cartId);
    }
}