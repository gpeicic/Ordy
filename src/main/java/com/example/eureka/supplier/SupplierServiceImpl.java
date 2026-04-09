package com.example.eureka.supplier;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierMapper supplierMapper;
    public SupplierServiceImpl(SupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
    }
    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierMapper.findAll();
    }
    @Override
    public List<Supplier> getSuppliersByCompany(Long companyId) {
        return supplierMapper.findSuppliersFromCompany(companyId);
    }
}
