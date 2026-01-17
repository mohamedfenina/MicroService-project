package com.irrigation.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server Application
 * 
 * Service Discovery and Registry for all microservices.
 * Provides a dashboard to monitor registered services.
 * 
 * Key Features:
 * - Service Registration: Microservices register themselves here
 * - Service Discovery: Services can discover each other
 * - Health Monitoring: Tracks health status of registered services
 * - Load Balancing: Provides service instance information for load balancing
 * 
 * @author Irrigation System Team
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        System.out.println("===========================================");
        System.out.println("✅ Eureka Server Started Successfully!");
        System.out.println("📍 Port: 8761");
        System.out.println("🌐 Dashboard: http://localhost:8761");
        System.out.println("📋 Services will register here");
        System.out.println("===========================================");
    }
}
