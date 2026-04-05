package com.example.eureka.invoice;

import java.time.LocalDateTime;

public class Invoice {
    private Long id;
    private Long supplierId;
    private String invoiceNumber;
    private LocalDateTime invoiceDatetime;
    private Long externalDocumentId;
    private Long companyId;

    public Invoice(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateTime getInvoiceDatetime() {
        return invoiceDatetime;
    }

    public void setInvoiceDatetime(LocalDateTime invoiceDatetime) {
        this.invoiceDatetime = invoiceDatetime;
    }

    public Long getExternalDocumentId() {
        return externalDocumentId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setExternalDocumentId(Long externalDocumentId) {
        this.externalDocumentId = externalDocumentId;
    }
}
