package com.example.eureka.orders;

import com.example.eureka.orders.dto.OrderSummary;
import com.example.eureka.orders.dto.OrderWithSupplierNameDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order createOrder(Long companyId, Long supplierId, Long userId);
    void addItem(Long orderId, Long productId, BigDecimal quantity);
    void removeItem(Long itemId);
    void sendOrder(Long orderId) throws IOException;
    List<Order> getOrdersByUser(Long userId);
    List<Order> getOrdersByCompany(Long companyId);
    List<OrderItem> getOrderItems(Long orderId);
    OrderWithSupplierNameDTO findLatestByCompanyId(Long companyId);
    List<OrderSummary> getOrderSummariesByCompany(Long companyId);
}
