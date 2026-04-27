package com.example.eureka.analytics;

import com.example.eureka.analytics.dto.SupplierSpendingDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "Analitika potrošnje")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/supplier-spending")
    public ResponseEntity<List<SupplierSpendingDTO>> getSupplierSpending(
            @RequestParam Long companyId
    ) {
        return ResponseEntity.ok(analyticsService.getSupplierSpending(companyId));
    }

    @GetMapping("/current-month-spending/{companyId}")
    public ResponseEntity<BigDecimal> getCurrentMonthSpendingForCompany(
            @PathVariable Long companyId
    ) {
        return ResponseEntity.ok(analyticsService.getCurrentMonthSpendingForCompany(companyId));
    }
}