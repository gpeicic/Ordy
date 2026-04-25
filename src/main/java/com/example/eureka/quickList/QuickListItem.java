package com.example.eureka.quickList;

import java.math.BigDecimal;

public class QuickListItem {
    private Long id;
    private Long quickListId;
    private Long catalogueItemId;
    private String productName;
    private BigDecimal quantity;

    public QuickListItem(){}

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

    public Long getQuickListId() {
        return quickListId;
    }

    public void setQuickListId(Long quickListId) {
        this.quickListId = quickListId;
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
