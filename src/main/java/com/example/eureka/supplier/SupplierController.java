package com.example.eureka.supplier;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    private final SupplierService supplierService;
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
       return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Supplier>> getSuppliersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(supplierService.getSuppliersByCompany(companyId));
    }
}
