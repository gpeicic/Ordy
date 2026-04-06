package com.example.eureka.orders;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long catalogueItemId;
    private BigDecimal quantity;

    public OrderItem(){}
    public OrderItem(Long id, Long orderId, Long catalogueItemId, BigDecimal quantity) {
        this.id = id;
        this.orderId = orderId;
        this.catalogueItemId = catalogueItemId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCatalogueItemId() {
        return catalogueItemId;
    }

    public void setCatalogueItemId(Long catalogueItemId) {
        this.catalogueItemId = catalogueItemId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
