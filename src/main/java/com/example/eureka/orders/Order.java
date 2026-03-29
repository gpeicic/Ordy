package com.example.eureka.orders;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long companyId;
    private Long supplierId;
    private Long userId;
    private LocalDateTime createdAt;
    private String status;

    public Order(){}
    public Order(Long id, Long companyId, Long supplierId, Long userId, LocalDateTime createdAt, String status) {
        this.id = id;
        this.companyId = companyId;
        this.supplierId = supplierId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
