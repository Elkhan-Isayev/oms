package com.oms.order.service;

import com.oms.order.client.InventoryClient;
import com.oms.order.dto.OrderDto;
import com.oms.order.model.Order;
import com.oms.order.model.OrderStatus;
import com.oms.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    public Order createOrder(OrderDto orderDto, Long customerId) {
        // Check if product exists and has sufficient stock
        boolean hasStock = inventoryClient.checkAndReserveStock(orderDto.getProductId(), 1L);
        
        if (!hasStock) {
            logger.warn("Insufficient stock for product ID: {}", orderDto.getProductId());
            throw new IllegalStateException("Product is out of stock");
        }
        
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setProductId(orderDto.getProductId());
        order.setTrackingId(generateTrackingId());
        order.setOrderStatus(OrderStatus.CREATED);
        
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        
        // Only created orders can be cancelled
        if (order.getOrderStatus() == OrderStatus.CREATED) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            
            // Release the reserved stock
            inventoryClient.releaseStock(order.getProductId(), 1L);
        } else {
            throw new IllegalStateException("Cannot cancel order with status: " + order.getOrderStatus());
        }
    }

    private String generateTrackingId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @PostConstruct
    private void logServiceStart() {
        logger.info("Order Service started successfully");
    }
}
