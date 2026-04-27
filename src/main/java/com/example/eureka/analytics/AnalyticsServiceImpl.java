package com.example.eureka.analytics;

import com.example.eureka.analytics.dto.ProductSpendingDTO;
import com.example.eureka.analytics.dto.SupplierSpendingDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{
    private final AnalyticsMapper analyticsMapper;

    public AnalyticsServiceImpl(AnalyticsMapper analyticsMapper) {
        this.analyticsMapper = analyticsMapper;
    }

    @Override
    public List<SupplierSpendingDTO> getSupplierSpending(Long companyId) {
        List<SupplierSpendingDTO> suppliers = analyticsMapper.getAvgMonthlySpendingPerSupplier(companyId);

        for (SupplierSpendingDTO supplier : suppliers) {
            BigDecimal currentMonth = analyticsMapper.getCurrentMonthSpending(companyId, supplier.getSupplierId());
            List<ProductSpendingDTO> products = analyticsMapper.getProductBreakdownForSupplier(companyId, supplier.getSupplierId());

            supplier.setCurrentMonthSpending(currentMonth != null ? currentMonth : BigDecimal.ZERO);
            supplier.setTopProducts(products);
        }

        suppliers.sort((a, b) -> b.getCurrentMonthSpending().compareTo(a.getCurrentMonthSpending()));

        return suppliers;
    }

    @Override
    public BigDecimal getCurrentMonthSpendingForCompany(Long companyId) {
        BigDecimal currentMonthSpending = analyticsMapper.getCurrentMonthSpendingForCompany(companyId);
        return currentMonthSpending != null ? currentMonthSpending : BigDecimal.ZERO;
    }

}
