package com.example.eureka.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderSummary {
    private Long id;
    private String supplierName;
    private LocalDateTime createdAt;
    private String status;
    private List<OrderItemDetail> items;

    public OrderSummary(){}

    public OrderSummary(Long id, String supplierName, LocalDateTime createdAt, String status, List<OrderItemDetail> items) {
        this.id = id;
        this.supplierName = supplierName;
        this.createdAt = createdAt;
        this.status = status;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemDetail> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDetail> items) {
        this.items = items;
    }
}
