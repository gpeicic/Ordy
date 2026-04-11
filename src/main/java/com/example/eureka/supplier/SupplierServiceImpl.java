package com.example.eureka.supplier;

import com.example.eureka.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierMapper supplierMapper;
    private final CompanySuppliersMapper companySuppliersMapper;
    public SupplierServiceImpl(SupplierMapper supplierMapper, CompanySuppliersMapper companySuppliersMapper) {
        this.supplierMapper = supplierMapper;
        this.companySuppliersMapper = companySuppliersMapper;
    }
    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierMapper.findAll();
    }
    @Override
    public List<Supplier> getSuppliersByCompany(Long companyId) {
        if (companyId == null) {
            throw new ValidationException("companyId je obavezan");
        }
        return companySuppliersMapper.findSuppliersFromCompany(companyId);
    }
}
