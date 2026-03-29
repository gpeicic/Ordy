package com.example.eureka.orders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long companyId,
            @RequestParam Long supplierId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(orderService.createOrder(companyId, supplierId, userId));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Void> addItem(
            @PathVariable Long orderId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantity
    ) {
        orderService.addItem(orderId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        orderService.removeItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/send")
    public ResponseEntity<Void> sendOrder(@PathVariable Long orderId) throws IOException {
        orderService.sendOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Order>> getOrdersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(orderService.getOrdersByCompany(companyId));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderItems(orderId));
    }
}
