package com.example.eureka.priceComparison;

import java.util.List;

public interface PriceComparisonService {
    List<PriceComparisonItem> getSupplierPriceComparisonForAllProducts(Long companyId, Long supplierId);
    List<PriceComparisonItem> getSupplierPriceComparisonForTopFive(Long companyId, Long supplierId);
    List<PriceComparisonItem> getProductPriceAcrossSuppliers(Long productId);
    List<PriceComparisonItem> getPriceComparisonAcrossCompanies(List<Long> companyIds, Long supplierId);
    List<PriceComparisonItem> getProductPriceAcrossSuppliersAndCompanies(Long productId);
}
