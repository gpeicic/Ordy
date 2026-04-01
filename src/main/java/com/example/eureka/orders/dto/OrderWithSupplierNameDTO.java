package com.example.eureka.orders.dto;

import java.time.LocalDateTime;

public class OrderWithSupplierNameDTO {
        private Long id;
        private Long companyId;
        private Long supplierId;
        private Long userId;
        private LocalDateTime createdAt;
        private String status;
        private String supplierName;

        public OrderWithSupplierNameDTO(Long id, Long companyId, Long supplierId, Long userId, LocalDateTime createdAt, String status, String supplierName) {
        this.id = id;
        this.companyId = companyId;
        this.supplierId = supplierId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.status = status;
        this.supplierName = supplierName;
        }

    public Long getId() {
        return id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public String getSupplierName() {
        return supplierName;
    }
}
