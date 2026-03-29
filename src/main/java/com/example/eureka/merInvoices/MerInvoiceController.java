package com.example.eureka.merInvoices;

import com.example.eureka.merInvoices.dto.InvoiceSummary;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class MerInvoiceController {

    private final MerInvoiceService merInvoiceService;

    public MerInvoiceController(MerInvoiceService merInvoiceService) {
        this.merInvoiceService = merInvoiceService;
    }

    @GetMapping("/{companyId}")
    public List<InvoiceSummary> getInvoices(@PathVariable Long companyId) {
        return merInvoiceService.getReceivedInvoices(companyId);
    }
    @GetMapping("/{companyId}/{documentId}")
    public ResponseEntity<String> downloadXml(
            @PathVariable Long companyId,
            @PathVariable Long documentId
    ) {

        String xml = merInvoiceService.downloadXml(companyId, documentId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xml);
    }
}
