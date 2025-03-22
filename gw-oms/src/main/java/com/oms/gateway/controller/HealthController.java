package com.oms.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Mono<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "OMS Gateway");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return Mono.just(response);
    }

    @GetMapping("/api/public/health")
    public Mono<Map<String, String>> publicHealth() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "OMS Gateway - Public Endpoint");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return Mono.just(response);
    }
}