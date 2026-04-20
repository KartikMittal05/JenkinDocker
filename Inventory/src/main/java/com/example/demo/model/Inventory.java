package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Inventory {

    @Id
    private Long pid;
    private int noOfItemsLeft;

    public Long getPid() { return pid; }
    public void setPid(Long pid) { this.pid = pid; }

    public int getNoOfItemsLeft() { return noOfItemsLeft; }
    public void setNoOfItemsLeft(int noOfItemsLeft) {
        this.noOfItemsLeft = noOfItemsLeft;
    }
}