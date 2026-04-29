package com.example.eureka.orders;

import com.example.eureka.catalogue.dto.SearchItemForOrderDTO;
import com.example.eureka.orders.command.OrderCommandService;
import com.example.eureka.orders.dispatch.OrderDispatchService;
import com.example.eureka.orders.dto.OrderSummary;
import com.example.eureka.orders.dto.OrderWithSupplierNameDTO;
import com.example.eureka.orders.query.OrderQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Tag(name = "Order", description = "Pristup narudzbenicama")
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
            @RequestParam Long userId,
            @RequestParam Long venueId
    ) {
        return ResponseEntity.ok(orderCommandService.createOrder(companyId, supplierId, userId,venueId));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Void> addItem(
            @PathVariable Long orderId,
            @RequestParam(required = false) Long catalogueItemId,
            @RequestParam BigDecimal quantity,
            @RequestParam String productName
    ) {
        orderCommandService.addItem(orderId, catalogueItemId,productName, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        orderCommandService.removeItem(itemId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/send")
    public ResponseEntity<Void> sendOrder(@PathVariable Long orderId, @RequestParam(defaultValue = "false") boolean hideCompanyName) throws IOException {
        System.out.println("controller " + hideCompanyName);
        orderDispatchService.sendOrder(orderId, hideCompanyName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderQueryService.getOrdersByUser(userId));
    }

    @GetMapping("/lastOrder/{companyId}")
    public ResponseEntity<OrderWithSupplierNameDTO> getLastOrder(@PathVariable Long companyId) {
        OrderWithSupplierNameDTO order = orderQueryService.getLatestByCompanyId(companyId);

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

    @GetMapping(value = "/confirm/{id}", produces = "text/html")
    public ResponseEntity<String> confirmOrder(@PathVariable Long id) {
        String status = orderDispatchService.confirmOrder(id);

        String html = switch (status) {
            case "NOT_FOUND" -> "<html>...<h2>❌ Narudžba nije pronađena</h2>...</html>";
            case "ALREADY_CONFIRMED" -> "<html>...<h2>⚠️ Već potvrđeno</h2>...</html>";
            default -> "<html>...<h1>✅ Potvrđeno!</h1><script>setTimeout(() => window.close(), 2000);</script>...</html>";
        };

        return ResponseEntity.ok(html);
    }
    @GetMapping("/{supplierId}/available-items")
    public ResponseEntity<List<SearchItemForOrderDTO>> getAvailableItems(
            @PathVariable Long supplierId,
            @RequestParam Long companyId,
            @RequestParam String name
    ) {
        return ResponseEntity.ok(orderQueryService.fuzzySearchByName(supplierId, companyId,name));
    }
}
