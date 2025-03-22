package com.oms.auth.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String passwordHash;
    private String provider;
    private String providerId;
    private Role role;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
