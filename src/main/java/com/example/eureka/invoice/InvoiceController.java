package com.example.eureka.invoice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{companyId}/top-supplier")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Long> getTopSupplier(@PathVariable Long companyId) {
        return ResponseEntity.ok(invoiceService.getTopSupplierId(companyId));
    }
    @GetMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public BigDecimal getMonthlySpending(@PathVariable Long companyId) {
        return invoiceService.getMonthlySpending(companyId);
    }
}
