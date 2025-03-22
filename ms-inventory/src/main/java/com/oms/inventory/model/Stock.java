package com.oms.inventory.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Stock {
    private Long id;
    private Long productId;
    private Long warehouseId;
    private Integer quantityAvailable;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
