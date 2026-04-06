package com.example.eureka.orders.command;

import com.example.eureka.orders.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderCommandServiceImpl implements OrderCommandService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderCommandServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public Order createOrder(Long companyId, Long supplierId, Long userId) {
        Order order = new Order();
        order.setCompanyId(companyId);
        order.setSupplierId(supplierId);
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(String.valueOf(OrderStatus.KREIRANO));
        orderMapper.insert(order);
        return order;
    }

    @Override
    public void addItem(Long orderId, Long catalogueItemId, BigDecimal quantity) {
        OrderItem item = new OrderItem();
        item.setOrderId(orderId);
        item.setCatalogueItemId(catalogueItemId);
        item.setQuantity(quantity);
        orderItemMapper.insert(item);
    }

    @Override
    public void removeItem(Long itemId) {
        orderItemMapper.delete(itemId);
    }
}

