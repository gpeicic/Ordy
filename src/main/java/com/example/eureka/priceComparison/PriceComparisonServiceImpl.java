package com.example.eureka.priceComparison;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceComparisonServiceImpl implements PriceComparisonService {

    private final PriceComparisonMapper priceComparisonMapper;

    public PriceComparisonServiceImpl(PriceComparisonMapper priceComparisonMapper) {
        this.priceComparisonMapper = priceComparisonMapper;
    }
    @Override
    public List<PriceComparisonItem> getSupplierPriceComparisonForAllProducts(Long companyId, Long supplierId){
        return priceComparisonMapper.getPriceComparisonForAllProducts(companyId, supplierId);
    }

    @Override
    public List<PriceComparisonItem> getSupplierPriceComparisonForTopFive(Long companyId, Long supplierId) {
        return priceComparisonMapper.getPriceComparisonForTopFive(companyId, supplierId);
    }

    @Override
    public List<PriceComparisonItem> getProductPriceAcrossSuppliers(Long productId){
        return priceComparisonMapper.getProductPriceAcrossSuppliers(productId);
    }
    @Override
    public List<PriceComparisonItem> getPriceComparisonAcrossCompanies(List<Long> companyIds, Long supplierId) {
        String ids = companyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return priceComparisonMapper.getPriceComparisonAcrossCompanies(ids, supplierId);
    }
}