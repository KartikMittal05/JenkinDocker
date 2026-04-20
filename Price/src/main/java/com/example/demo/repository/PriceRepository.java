package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {}
