package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {}
