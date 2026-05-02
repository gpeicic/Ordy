package com.example.eureka.priceComparison;

import com.example.eureka.exception.ValidationException;
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
    public List<PriceComparisonItem> getSupplierPriceComparisonForAllProducts(Long companyId, Long supplierId) {
        if (companyId == null || supplierId == null) {
            throw new ValidationException("companyId i supplierId su obavezni");
        }
        return priceComparisonMapper.getPriceComparisonForAllProducts(companyId, supplierId);
    }
    @Override
    public List<PriceComparisonItem> getProductPriceAcrossSuppliersAndCompanies(Long productId) {
        if (productId == null) throw new ValidationException("productId je obavezan");
        return priceComparisonMapper.getProductPriceAcrossSuppliersAndCompanies(productId);
    }


    @Override
    public List<PriceComparisonItem> getSupplierPriceComparisonForTopFive(Long companyId, Long supplierId) {
        if (companyId == null || supplierId == null) {
            throw new ValidationException("companyId i supplierId su obavezni");
        }
        return priceComparisonMapper.getPriceComparisonForTopFive(companyId, supplierId);
    }

    @Override
    public List<PriceComparisonItem> getProductPriceAcrossSuppliers(Long productId) {
        if (productId == null) {
            throw new ValidationException("productId je obavezan");
        }
        return priceComparisonMapper.getProductPriceAcrossSuppliers(productId);
    }

    @Override
    public List<PriceComparisonItem> getPriceComparisonAcrossCompanies(List<Long> companyIds, Long supplierId) {
        if (companyIds == null || companyIds.isEmpty()) {
            throw new ValidationException("Lista kompanija je prazna");
        }
        if (supplierId == null) {
            throw new ValidationException("supplierId je obavezan");
        }
        String ids = companyIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return priceComparisonMapper.getPriceComparisonAcrossCompanies(ids, supplierId);
    }

}