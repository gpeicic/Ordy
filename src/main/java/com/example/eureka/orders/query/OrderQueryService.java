package com.example.eureka.orders.query;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import com.example.eureka.orders.Order;
import com.example.eureka.orders.OrderItem;
import com.example.eureka.orders.dto.OrderSummary;
import com.example.eureka.orders.dto.OrderWithSupplierNameDTO;

import java.util.List;

public interface OrderQueryService {
    List<Order> getOrdersByUser(Long userId);
    List<Order> getOrdersByCompany(Long companyId);
    List<OrderItem> getOrderItems(Long orderId);
    OrderWithSupplierNameDTO findLatestByCompanyId(Long companyId);
    List<OrderSummary> getOrderSummariesByCompany(Long companyId);
    List<SearchItemForOrderDTO> fuzzySearchByName(Long supplierId,Long companyId, String name);
}
