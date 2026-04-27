package com.example.eureka.analytics;

import com.example.eureka.analytics.dto.SupplierSpendingDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AnalyticsService {
    List<SupplierSpendingDTO> getSupplierSpending(Long companyId);
    BigDecimal getCurrentMonthSpendingForCompany(Long companyId);
}
