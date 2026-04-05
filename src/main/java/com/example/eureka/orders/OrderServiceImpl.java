package com.example.eureka.orders;

import com.example.eureka.orders.dto.OrderItemDetail;
import com.example.eureka.orders.dto.OrderSummary;
import com.example.eureka.orders.dto.OrderWithSupplierNameDTO;
import com.example.eureka.orders.pdfGenerator.MailService;
import com.example.eureka.orders.pdfGenerator.PdfGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final PdfGeneratorService pdfGeneratorService;
    private final MailService mailService;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper, PdfGeneratorService pdfGeneratorService, MailService mailService) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.pdfGeneratorService = pdfGeneratorService;
        this.mailService = mailService;
    }

    @Override
    public Order createOrder(Long companyId, Long supplierId, Long userId) {
        Order order = new Order();
        order.setCompanyId(companyId);
        order.setSupplierId(supplierId);
        order.setUserId(userId);
        order.setCreatedAt(LocalDateTime.now()); //
        order.setStatus(String.valueOf(OrderStatus.KREIRANO));
        orderMapper.insert(order);
        return order;
    }

    @Override
    public void addItem(Long orderId, Long productId, BigDecimal quantity) {
        OrderItem item = new OrderItem();
        item.setOrderId(orderId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        orderItemMapper.insert(item);
    }

    @Override
    public void removeItem(Long itemId) {
        orderItemMapper.delete(itemId);
    }

    @Override
    @Transactional
    public void sendOrder(Long orderId) throws IOException {
        Order order = orderMapper.findById(orderId);
        byte[] pdfBytes = pdfGeneratorService.generateOrderPdf(order);
        mailService.sendOrderPdf(order, pdfBytes);
        orderMapper.updateStatus(orderId, String.valueOf(OrderStatus.POSLANO));
    }

    @Override
    public OrderWithSupplierNameDTO findLatestByCompanyId(Long companyId) {
        return orderMapper.findLatestByCompanyId(companyId);
    }

    @Override
    public List<OrderSummary> getOrderSummariesByCompany(Long companyId) {
        List<OrderSummary> summaries = orderMapper.findSummariesByCompanyId(companyId);

        summaries.forEach(summary -> {
            List<OrderItem> items = orderItemMapper.findByOrderId(summary.getId());
            List<OrderItemDetail> details = items.stream()
                    .map(item -> {
                        String name = orderItemMapper.findProductNameByProductId(item.getProductId());
                        OrderItemDetail detail = new OrderItemDetail();
                        detail.setId(item.getId());
                        detail.setProductName(name != null ? name : "Nepoznat artikl");
                        detail.setQuantity(item.getQuantity());
                        return detail;
                    })
                    .collect(Collectors.toList());
            summary.setItems(details);
        });

        return summaries;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByCompany(Long companyId) {
        return orderMapper.findByCompanyId(companyId);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }
}
