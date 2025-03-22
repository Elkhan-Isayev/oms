package com.oms.gateway.direct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.oms.gateway")
public class DirectGatewayApplication {
    public static void main(String[] args) {
        System.out.println("Starting Direct Gateway Application on port 5000...");
        new SpringApplicationBuilder(DirectGatewayApplication.class)
            .web(WebApplicationType.REACTIVE)
            .run(args);
        System.out.println("Direct Gateway Application started!");
    }
}
