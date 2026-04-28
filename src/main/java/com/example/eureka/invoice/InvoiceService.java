package com.example.eureka.invoice;

import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface InvoiceService {
    Long getTopSupplierId(Long companyId);
    BigDecimal getMonthlySpending(Long companyId);
    void saveParsedInvoice(ParsedInvoice parsed, Long externalDocumentId, Long companyId);
    Long getMaxExternalDocumentId(Long companyId);
}
