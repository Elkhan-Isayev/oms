package com.oms.auth.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Permission {
    private Long id;
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
