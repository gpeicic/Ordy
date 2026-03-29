package com.example.eureka.merInvoices.parsedInvoice;

import com.example.eureka.merInvoices.parsedInvoice.dto.ParsedInvoice;

public interface InvoiceParser {
    ParsedInvoice parse(byte[] xmlBytes);
}
