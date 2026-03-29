package com.example.eureka.merInvoices;

import com.example.eureka.merInvoices.dto.InvoiceSummary;
import com.example.eureka.merInvoices.dto.SearchReceivedRequest;

import java.util.List;

public interface MerInvoiceService {
    List<InvoiceSummary> getReceivedInvoices(Long companyId);
    String downloadXml(Long companyId, Long documentId);
}
