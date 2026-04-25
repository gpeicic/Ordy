package com.example.eureka.quickList.dto;

import java.math.BigDecimal;

public class QuickListItemRequest {
    private Long catalogueItemId;
    private String productName;
    private BigDecimal quantity;

    public QuickListItemRequest(){}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
