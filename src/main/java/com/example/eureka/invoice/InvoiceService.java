package com.example.eureka.invoice;

import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;

import java.math.BigDecimal;

public interface InvoiceService {
    BigDecimal getMonthlySpending(Long companyId);
    void saveParsedInvoice(ParsedInvoice parsed, Long externalDocumentId, Long companyId);
}
