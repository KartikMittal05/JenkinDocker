package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ProductCatalog;
import com.example.demo.service.CatalogService;

@RestController
@RequestMapping("/catalogs/products")
public class CatalogController {

    @Autowired
    private CatalogService service;

    @GetMapping("/{id}")
    public ProductCatalog get(@PathVariable Long id) {
        return service.getCatalog(id);
    }

    @GetMapping("/category/{category}")
    public List<ProductCatalog> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }
}
