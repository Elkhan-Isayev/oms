package com.oms.swagger.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RestController
public class SwaggerConfig {

    @Value("${services.auth.url:http://localhost:5001}")
    private String authServiceUrl;
    
    @Value("${services.order.url:http://localhost:5002}")
    private String orderServiceUrl;
    
    @Value("${services.inventory.url:http://localhost:5003}")
    private String inventoryServiceUrl;

    @GetMapping("/")
    public RedirectView redirectToSwaggerUi() {
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("/api/swagger/services")
    public Map<String, String> getServiceUrls() {
        Map<String, String> serviceUrls = new HashMap<>();
        serviceUrls.put("auth", authServiceUrl + "/v3/api-docs");
        serviceUrls.put("order", orderServiceUrl + "/v3/api-docs");
        serviceUrls.put("inventory", inventoryServiceUrl + "/v3/api-docs");
        return serviceUrls;
    }
}
