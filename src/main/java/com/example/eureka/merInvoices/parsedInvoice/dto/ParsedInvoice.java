package com.example.eureka.merInvoices.parsedInvoice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ParsedInvoice {
    private String supplierName;
    private String oib;
    private String interniBroj;
    private LocalDateTime invoiceDateTime;
    private List<ParsedInvoiceItem> items;

    public ParsedInvoice(String supplierName,
                         LocalDateTime invoiceDateTime,
                         List<ParsedInvoiceItem> items,
                         String oib,
                         String interniBroj) {
        this.supplierName = supplierName;
        this.invoiceDateTime = invoiceDateTime;
        this.items = items;
        this.oib = oib;
        this.interniBroj = interniBroj;
    }

    public String getInterniBroj() {
        return interniBroj;
    }

    public void setInterniBroj(String interniBroj) {
        this.interniBroj = interniBroj;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public LocalDateTime getInvoiceDateTime() {
        return invoiceDateTime;
    }

    public void setInvoiceDateTime(LocalDateTime invoiceDateTime) {
        this.invoiceDateTime = invoiceDateTime;
    }

    public List<ParsedInvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<ParsedInvoiceItem> items) {
        this.items = items;
    }
}
