package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ProductCatalog;
import com.example.demo.service.RecommendationService;



@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService service;

    @PostMapping
    public List<ProductCatalog> recommend(@RequestBody List<Long> pids) {
        return service.recommend(pids);
    }
}