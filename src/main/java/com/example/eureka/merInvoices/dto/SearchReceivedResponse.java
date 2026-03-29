package com.example.eureka.merInvoices.dto;

import java.util.List;

public class SearchReceivedResponse {
    private List<InvoiceSummary> items;

    public List<InvoiceSummary> getItems() {
        return items;
    }

    public void setItems(List<InvoiceSummary> items) {
        this.items = items;
    }
}
