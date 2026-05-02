package com.example.eureka.priceComparison;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "PriceComparison", description = "Usporedba cijena")
@RequestMapping("/price-comparison")
public class PriceComparisonController {

    private final PriceComparisonService priceComparisonService;

    public PriceComparisonController(PriceComparisonService priceComparisonService) {
        this.priceComparisonService = priceComparisonService;
    }
    @GetMapping("/allProducts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVIDER')")
    public ResponseEntity<List<PriceComparisonItem>> getSupplierComparisonForAllProducts(
            @RequestParam Long companyId,
            @RequestParam Long supplierId
    ) {
        return ResponseEntity.ok(
                priceComparisonService.getSupplierPriceComparisonForAllProducts(companyId, supplierId)
        );
    }

    @GetMapping("/supplier")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROVIDER')")
    public ResponseEntity<List<PriceComparisonItem>> getSupplierComparisonForTopFive(
            @RequestParam Long companyId,
            @RequestParam Long supplierId
    ) {
        return ResponseEntity.ok(
                priceComparisonService.getSupplierPriceComparisonForTopFive(companyId, supplierId)
        );
    }

    @GetMapping("/product")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PriceComparisonItem>> getProductComparison(
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(
                priceComparisonService.getProductPriceAcrossSuppliers(productId)
        );
    }
    @GetMapping("/acrossCompanies")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<PriceComparisonItem>> getPriceComparisonAcrossCompanies(
            @RequestParam List<Long> companyIds,
            @RequestParam Long supplierId
    ) {
        return ResponseEntity.ok(
                priceComparisonService.getPriceComparisonAcrossCompanies(companyIds, supplierId)
        );
    }

    @GetMapping("/product/full")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PriceComparisonItem>> getProductComparisonFull(
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(
                priceComparisonService.getProductPriceAcrossSuppliersAndCompanies(productId)
        );
    }
}