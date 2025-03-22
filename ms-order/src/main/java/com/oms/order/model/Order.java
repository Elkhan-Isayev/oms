package com.oms.order.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private Long customerId;
    private Long productId;
    private String trackingId;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
