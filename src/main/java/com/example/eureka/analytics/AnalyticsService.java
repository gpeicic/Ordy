package com.example.eureka.analytics;

import com.example.eureka.analytics.dto.SupplierSpendingDTO;

import java.util.List;

public interface AnalyticsService {
    List<SupplierSpendingDTO> getSupplierSpending(Long companyId);
}
