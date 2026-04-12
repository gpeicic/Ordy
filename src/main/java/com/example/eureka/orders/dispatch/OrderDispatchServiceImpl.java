package com.example.eureka.orders.dispatch;

import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.orders.Order;
import com.example.eureka.orders.OrderMapper;
import com.example.eureka.orders.OrderStatus;
import com.example.eureka.orders.mail.MailService;
import com.example.eureka.orders.pdfGenerator.PdfGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class OrderDispatchServiceImpl implements OrderDispatchService{

    private static final Logger log = LoggerFactory.getLogger(OrderDispatchServiceImpl.class);

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
        log.info("Slanje narudžbe — orderId: {}", orderId);
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            log.error("Narudžba nije pronađena — orderId: {}", orderId);
            throw new ResourceNotFoundException("Narudžba nije pronađena: " + orderId);
        }
        byte[] pdfBytes = pdfGeneratorService.generateOrderPdf(order);
        mailService.sendOrderPdf(order, pdfBytes);
        orderMapper.updateStatus(orderId, String.valueOf(OrderStatus.POSLANO));
        log.info("Narudžba uspješno poslana — orderId: {}, companyId: {}, supplierId: {}", orderId, order.getCompanyId(), order.getSupplierId());
    }

    @Override
    @Transactional
    public String confirmOrder(Long orderId) {
        log.info("Potvrda narudžbe — orderId: {}", orderId);
        Order order = orderMapper.findById(orderId);

        if (order == null) {
            log.warn("Potvrda odbijena — narudžba nije pronađena: {}", orderId);
            return "NOT_FOUND";
        }

        if (OrderStatus.POTVRĐENO.name().equals(order.getStatus())) {
            log.warn("Potvrda odbijena — narudžba već potvrđena: {}", orderId);
            return "ALREADY_CONFIRMED";
        }

        orderMapper.updateStatus(orderId, String.valueOf(OrderStatus.POTVRĐENO));
        log.info("Narudžba potvrđena — orderId: {}", orderId);
        return "CONFIRMED";
    }
}
