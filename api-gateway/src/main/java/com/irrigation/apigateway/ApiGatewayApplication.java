package com.irrigation.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway Application
 * 
 * Single entry point for all client requests to the microservices.
 * 
 * Key Responsibilities:
 * - Request Routing: Routes requests to appropriate microservices
 * - Load Balancing: Distributes load across service instances
 * - Service Discovery: Uses Eureka to find service instances
 * - Gateway Filters: Can add cross-cutting concerns (auth, logging, etc.)
 * 
 * Routing Pattern:
 * - /api/energy/** → Energy Service
 * - /api/water/** → Water Service
 * 
 * @author Irrigation System Team
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("===========================================");
        System.out.println("✅ API Gateway Started Successfully!");
        System.out.println("📍 Port: 8080");
        System.out.println("🌐 Gateway URL: http://localhost:8080");
        System.out.println("🔀 Routes:");
        System.out.println("   → /api/energy/** → Energy Service");
        System.out.println("   → /api/water/** → Water Service");
        System.out.println("===========================================");
    }
}
