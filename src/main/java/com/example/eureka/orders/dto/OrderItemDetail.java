package com.example.eureka.orders.dto;

import java.math.BigDecimal;

public class OrderItemDetail {
    private Long id;
    private String name;
    private BigDecimal quantity;

    public OrderItemDetail(Long id, String name, BigDecimal quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
    public OrderItemDetail(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
