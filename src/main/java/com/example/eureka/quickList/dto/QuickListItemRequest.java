package com.example.eureka.quickList.dto;

import java.math.BigDecimal;

public class QuickListItemRequest {
    private Long catalogueItemId;
    private BigDecimal quantity;

    public QuickListItemRequest(){}

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
