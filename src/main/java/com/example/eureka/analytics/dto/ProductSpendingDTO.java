package com.example.eureka.analytics.dto;

import java.math.BigDecimal;

public class ProductSpendingDTO {
    private String productName;
    private BigDecimal totalQuantity;
    private BigDecimal totalSpent;

    public ProductSpendingDTO(){}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
}
