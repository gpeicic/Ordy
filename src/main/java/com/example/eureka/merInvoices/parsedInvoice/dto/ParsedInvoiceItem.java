package com.example.eureka.merInvoices.parsedInvoice.dto;

import java.math.BigDecimal;

public class ParsedInvoiceItem {
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal amount;

    public ParsedInvoiceItem(String productName, BigDecimal unitPrice,
                             BigDecimal discount, BigDecimal amount) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
