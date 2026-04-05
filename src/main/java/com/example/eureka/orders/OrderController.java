package com.example.eureka.orders;

import com.example.eureka.orders.command.OrderCommandService;
import com.example.eureka.orders.dispatch.OrderDispatchService;
import com.example.eureka.orders.dto.OrderSummary;
import com.example.eureka.orders.dto.OrderWithSupplierNameDTO;
import com.example.eureka.orders.query.OrderQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderDispatchService orderDispatchService;
    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    public OrderController(OrderDispatchService orderDispatchService, OrderQueryService orderQueryService, OrderCommandService orderCommandService) {
        this.orderDispatchService = orderDispatchService;
        this.orderQueryService = orderQueryService;
        this.orderCommandService = orderCommandService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long companyId,
            @RequestParam Long supplierId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(orderCommandService.createOrder(companyId, supplierId, userId));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Void> addItem(
            @PathVariable Long orderId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantity
    ) {
        orderCommandService.addItem(orderId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        orderCommandService.removeItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/send")
    public ResponseEntity<Void> sendOrder(@PathVariable Long orderId) throws IOException {
        orderDispatchService.sendOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderQueryService.getOrdersByUser(userId));
    }

    @GetMapping("/lastOrder/{companyId}")
    public ResponseEntity<OrderWithSupplierNameDTO> getLastOrder(@PathVariable Long companyId) {
        OrderWithSupplierNameDTO order = orderQueryService.findLatestByCompanyId(companyId);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<OrderSummary>> getOrdersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(orderQueryService.getOrderSummariesByCompany(companyId));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderQueryService.getOrderItems(orderId));
    }
}
