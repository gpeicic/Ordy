package com.example.eureka.invoice;

import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;

public interface InvoiceService {
    void saveParsedInvoice(ParsedInvoice parsed, Long externalDocumentId, Long companyId);
}
