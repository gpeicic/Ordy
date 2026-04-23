package com.example.eureka.orders.command;

import com.example.eureka.exception.ValidationException;
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
    public Order createOrder(Long companyId, Long supplierId, Long userId, Long venueId) {
        if (companyId == null || supplierId == null || userId == null || venueId == null) {
            throw new ValidationException("companyId, supplierId, userId i venueId su obavezni");
        }
        Order order = new Order();
        order.setCompanyId(companyId);
        order.setSupplierId(supplierId);
        order.setUserId(userId);
        order.setVenueId(venueId);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(String.valueOf(OrderStatus.KREIRANO));
        orderMapper.insert(order);
        return order;
    }

    @Override
    public void addItem(Long orderId, Long catalogueItemId,String productName, BigDecimal quantity) {
        if (orderId == null) {
            throw new ValidationException("orderId je obavezan");
        }
        if (catalogueItemId == null && (productName == null || productName.isBlank())) {
            throw new ValidationException("Mora biti catalogueItemId ili productName");
        }
        OrderItem item = new OrderItem();
        item.setOrderId(orderId);
        item.setCatalogueItemId(catalogueItemId);
        item.setProductName(productName);
        item.setQuantity(quantity != null ? quantity : BigDecimal.ONE);
        item.setSource(catalogueItemId != null ? "CATALOGUE" : "INVOICE");
        orderItemMapper.insert(item);
    }

    @Override
    public void removeItem(Long itemId) {
        orderItemMapper.delete(itemId);
    }
}

