package com.oms.order.controller;

import com.oms.order.dto.OrderDto;
import com.oms.order.model.Order;
import com.oms.order.model.OrderStatus;
import com.oms.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "Endpoints for order management")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto, @RequestHeader("X-User-Id") String userId) {
        Long customerId = Long.parseLong(userId);
        Order newOrder = orderService.createOrder(orderDto, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping
    @Operation(summary = "Get all orders for the current customer")
    public ResponseEntity<List<Order>> getCustomerOrders(@RequestHeader("X-User-Id") String userId) {
        Long customerId = Long.parseLong(userId);
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, @RequestHeader("X-User-Id") String userId,
                                              @RequestHeader("X-User-Role") String role) {
        Long customerId = Long.parseLong(userId);
        Order order = orderService.getOrderById(id);
        
        // Check if the user has access to this order
        if ("ADMIN".equals(role) || order.getCustomerId().equals(customerId)) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, 
                                                 @RequestParam OrderStatus status,
                                                 @RequestHeader("X-User-Role") String role) {
        // Only admin can update order status
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel an order")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id, 
                                          @RequestHeader("X-User-Id") String userId,
                                          @RequestHeader("X-User-Role") String role) {
        Long customerId = Long.parseLong(userId);
        Order order = orderService.getOrderById(id);
        
        // Check if the user has access to cancel this order
        if ("ADMIN".equals(role) || order.getCustomerId().equals(customerId)) {
            orderService.cancelOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
