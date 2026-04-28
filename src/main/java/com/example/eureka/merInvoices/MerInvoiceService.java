package com.example.eureka.merInvoices;

import com.example.eureka.merInvoices.dto.InvoiceSummary;

import java.util.List;

public interface MerInvoiceService {
    List<InvoiceSummary> getReceivedInvoices(Long companyId);
    String downloadXml(Long companyId, Long documentId);
    void firstSync(Long companyId);
    void syncNewInvoices(Long companyId);
}
