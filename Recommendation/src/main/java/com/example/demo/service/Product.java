package com.example.demo.service;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    private Long pid;
    private String pname;
    private String pcategory;

    public Long getPid() { return pid; }
    public void setPid(Long pid) { this.pid = pid; }

    public String getPname() { return pname; }
    public void setPname(String pname) { this.pname = pname; }

    public String getPcategory() { return pcategory; }
    public void setPcategory(String pcategory) { this.pcategory = pcategory; }
}