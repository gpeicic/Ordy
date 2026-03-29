package com.example.eureka.priceComparison;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceComparisonItem {
    private String canonicalName;
    private String supplierName;
    private BigDecimal totalAmount;
    private BigDecimal latestPrice;
    private BigDecimal previousPrice;

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
}