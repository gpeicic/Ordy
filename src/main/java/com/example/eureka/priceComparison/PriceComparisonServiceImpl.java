package com.example.eureka.priceComparison;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceComparisonServiceImpl implements PriceComparisonService {

    private final PriceComparisonMapper priceComparisonMapper;

    public PriceComparisonServiceImpl(PriceComparisonMapper priceComparisonMapper) {
        this.priceComparisonMapper = priceComparisonMapper;
    }

    @Override
    public List<PriceComparisonItem> getSupplierPriceComparison(Long companyId, Long supplierId) {
        return priceComparisonMapper.getPriceComparison(companyId, supplierId);
    }

    @Override
    public List<PriceComparisonItem> getProductPriceAcrossSuppliers(Long productId){
        return priceComparisonMapper.getProductPriceAcrossSuppliers(productId);
    }
}