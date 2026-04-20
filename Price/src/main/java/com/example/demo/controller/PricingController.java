package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Price;
import com.example.demo.repository.PriceRepository;




@RestController
@RequestMapping("/prices")
public class PricingController {

    @Autowired
    private PriceRepository repo;

    @GetMapping("/{pid}")
    public Price get(@PathVariable Long pid) {
        return repo.findById(pid).orElse(null);
    }

    @PostMapping
    public Price add(@RequestBody Price p) {
        return repo.save(p);
    }
}