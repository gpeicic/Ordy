package com.example.eureka.orders.command;

import com.example.eureka.orders.Order;

import java.math.BigDecimal;

public interface OrderCommandService {
    Order createOrder(Long companyId, Long supplierId, Long userId);
    void addItem(Long orderId, Long productId, BigDecimal quantity);
    void removeItem(Long itemId);
}
