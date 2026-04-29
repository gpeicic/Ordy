package com.example.eureka.invoice;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Tag(name = "Invoice", description = "Pristup mjesecnoj potrosnji i zadnjem dobavljacu")
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{companyId}/top-supplier")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVIDER')")
    public ResponseEntity<Long> getTopSupplier(@PathVariable Long companyId) {
        Long supplierId = invoiceService.getTopSupplierId(companyId);
        if (supplierId == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(supplierId);
    }

    @GetMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVIDER')")
    public ResponseEntity<BigDecimal> getMonthlySpending(@PathVariable Long companyId) {
        return ResponseEntity.ok(invoiceService.getMonthlySpending(companyId));
    }
}
