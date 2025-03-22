package com.oms.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryClient.class);
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;
    
    public boolean checkAndReserveStock(Long productId, Long quantity) {
        try {
            String url = inventoryServiceUrl + "/api/stocks/reserve?productId=" + productId + "&quantity=" + quantity;
            ResponseEntity<Boolean> response = restTemplate.postForEntity(url, null, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            logger.error("Error checking stock availability: {}", e.getMessage());
            return false;
        }
    }
    
    public void releaseStock(Long productId, Long quantity) {
        try {
            String url = inventoryServiceUrl + "/api/stocks/release?productId=" + productId + "&quantity=" + quantity;
            restTemplate.postForEntity(url, null, Void.class);
        } catch (Exception e) {
            logger.error("Error releasing stock: {}", e.getMessage());
        }
    }
}
