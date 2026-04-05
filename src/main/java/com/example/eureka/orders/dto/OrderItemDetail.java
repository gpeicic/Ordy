package com.example.eureka.orders.dto;

import java.math.BigDecimal;

public class OrderItemDetail {
    private Long id;
    private String productName;
    private BigDecimal quantity;

    public OrderItemDetail(Long id, String productName, BigDecimal quantity) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
    }
    public OrderItemDetail(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
