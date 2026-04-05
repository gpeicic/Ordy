package com.example.eureka.orders.dispatch;

import com.example.eureka.orders.Order;
import com.example.eureka.orders.OrderMapper;
import com.example.eureka.orders.OrderStatus;
import com.example.eureka.orders.pdfGenerator.MailService;
import com.example.eureka.orders.pdfGenerator.PdfGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class OrderDispatchServiceImpl implements OrderDispatchService{
    private final OrderMapper orderMapper;
    private final PdfGeneratorService pdfGeneratorService;
    private final MailService mailService;

    public OrderDispatchServiceImpl(OrderMapper orderMapper, PdfGeneratorService pdfGeneratorService, MailService mailService) {
        this.orderMapper = orderMapper;
        this.pdfGeneratorService = pdfGeneratorService;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public void sendOrder(Long orderId) throws IOException {
        Order order = orderMapper.findById(orderId);
        byte[] pdfBytes = pdfGeneratorService.generateOrderPdf(order);
        mailService.sendOrderPdf(order, pdfBytes);
        orderMapper.updateStatus(orderId, String.valueOf(OrderStatus.POSLANO));
    }
}
