package com.example.eureka.priceComparison;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/price-comparison")
public class PriceComparisonController {

    private final PriceComparisonService priceComparisonService;

    public PriceComparisonController(PriceComparisonService priceComparisonService) {
        this.priceComparisonService = priceComparisonService;
    }

    @GetMapping("/supplier")
    public ResponseEntity<List<PriceComparisonItem>> getSupplierComparison(
            @RequestParam Long companyId,
            @RequestParam Long supplierId
    ) {
        return ResponseEntity.ok(
                priceComparisonService.getSupplierPriceComparison(companyId, supplierId)
        );
    }

    @GetMapping("/product")
    public ResponseEntity<List<PriceComparisonItem>> getProductComparison(
            @RequestParam Long productId
    ) {
        return ResponseEntity.ok(
                priceComparisonService.getProductPriceAcrossSuppliers(productId)
        );
    }

}