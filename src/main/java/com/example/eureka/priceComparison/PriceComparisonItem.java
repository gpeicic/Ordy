package com.example.eureka.priceComparison;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceComparisonItem {
    private String canonicalName;
    private String supplierName;
    private Long companyId;
    private BigDecimal totalAmount;
    private BigDecimal latestPrice;
    private BigDecimal previousPrice;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getPriceDifference() {
        if (latestPrice == null || previousPrice == null) return null;
        return latestPrice.subtract(previousPrice);
    }

    public BigDecimal getPriceChangePercent() {
        if (latestPrice == null || previousPrice == null || previousPrice.compareTo(BigDecimal.ZERO) == 0) return null;
        return latestPrice.subtract(previousPrice)
                .divide(previousPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(BigDecimal latestPrice) {
        this.latestPrice = latestPrice;
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(BigDecimal previousPrice) {
        this.previousPrice = previousPrice;
    }
}