package com.example.eureka.analytics.dto;

import java.math.BigDecimal;
import java.util.List;

public class SupplierSpendingDTO {
    private Long supplierId;
    private String supplierName;
    private BigDecimal avgMonthlySpending;
    private BigDecimal currentMonthSpending;
    private List<ProductSpendingDTO> topProducts;

    public SupplierSpendingDTO(){}

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getAvgMonthlySpending() {
        return avgMonthlySpending;
    }

    public void setAvgMonthlySpending(BigDecimal avgMonthlySpending) {
        this.avgMonthlySpending = avgMonthlySpending;
    }

    public BigDecimal getCurrentMonthSpending() {
        return currentMonthSpending;
    }

    public void setCurrentMonthSpending(BigDecimal currentMonthSpending) {
        this.currentMonthSpending = currentMonthSpending;
    }

    public List<ProductSpendingDTO> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<ProductSpendingDTO> topProducts) {
        this.topProducts = topProducts;
    }
}
