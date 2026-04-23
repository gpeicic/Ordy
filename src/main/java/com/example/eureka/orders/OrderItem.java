package com.example.eureka.orders;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long catalogueItemId;
    private BigDecimal quantity;
    private String productName;
    private String source;


    public OrderItem(){}
    public OrderItem(Long id, Long orderId, Long catalogueItemId, BigDecimal quantity, String productName) {
        this.id = id;
        this.orderId = orderId;
        this.catalogueItemId = catalogueItemId;
        this.quantity = quantity;
        this.productName = productName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
