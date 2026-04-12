package com.example.eureka.quickList.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class QuickListRequest {
    @NotBlank(message = "Naziv liste je obavezan")
    private String name;
    private Long companyId;
    private Long userId;
    private Long supplierId;
    @NotEmpty
    private List<QuickListItemRequest> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public List<QuickListItemRequest> getItems() {
        return items;
    }

    public void setItems(List<QuickListItemRequest> items) {
        this.items = items;
    }
}
