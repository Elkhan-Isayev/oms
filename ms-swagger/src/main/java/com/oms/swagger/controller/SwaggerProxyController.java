package com.oms.swagger.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/swagger/services")
public class SwaggerProxyController {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.auth.url}")
    private String authServiceUrl;
    
    @Value("${services.order.url}")
    private String orderServiceUrl;
    
    @Value("${services.inventory.url}")
    private String inventoryServiceUrl;

    public SwaggerProxyController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/{service}")
    public Mono<ResponseEntity<String>> getServiceApiDocs(@PathVariable String service) {
        String targetUrl;
        
        switch (service) {
            case "auth":
                targetUrl = authServiceUrl;
                break;
            case "order":
                targetUrl = orderServiceUrl;
                break;
            case "inventory":
                targetUrl = inventoryServiceUrl;
                break;
            default:
                return Mono.just(ResponseEntity.notFound().build());
        }
        
        return webClientBuilder.build()
                .get()
                .uri(targetUrl + "/v3/api-docs")
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    System.err.println("Error fetching API docs from " + targetUrl + ": " + e.getMessage());
                    return Mono.just(ResponseEntity.status(503)
                            .body("{'error': 'Service unavailable or does not support OpenAPI', 'message': '" 
                                  + e.getMessage() + "'}"));
                });
    }
}