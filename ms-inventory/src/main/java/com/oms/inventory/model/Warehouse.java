package com.oms.inventory.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Warehouse {
    private Long id;
    private String name;
    private String location;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
